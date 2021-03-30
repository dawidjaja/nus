package agent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import transformer.MethodTransformer;
import transformer.data.ClassTransformData;
import transformer.data.MethodTransformData;
import transformer.data.TransformData;

public class Agent {
    // A specification for the transformation, located in resources/<filename>
    private static final String TRANSFORM_CONFIG_FILENAME = "transform.yaml";

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[Agent] In premain method");
        System.out.println("[Agent] args: " + agentArgs);
        System.out.println("[Agent] is redefine supported : " + inst.isRedefineClassesSupported());
        System.out.println("[Agent] is retransform supported : " + inst.isRetransformClassesSupported());

        try {
            TransformData transformData = readYamlConfig(agentArgs);
            transformsBasedOnSpec(transformData, inst);
        } catch (IOException e) {
            System.err.println("Exception when reading the yaml file: " +  e);
        }
    }

    private static TransformData readYamlConfig(String filename) throws IOException {
        // Loading the YAML file from the /resources folder
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(filename);

        // Instantiating a new ObjectMapper as a YAMLFactory
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        // Mapping the employee from the YAML file to the TransformData class
        TransformData data = om.readValue(file, TransformData.class);
//        System.out.println(data);
        return data;
    }

    private static void transformsBasedOnSpec(TransformData transformData, Instrumentation inst) {
        List<ClassTransformData> transformations = transformData.getTransformations();
        // Insert some logic to methods
        for (ClassTransformData classTransformation : transformations) {
            transformClass(classTransformation, inst);
        }
    }

    private static void transformClass(ClassTransformData classTransformData, Instrumentation instrumentation) {
        // read class transformation specification
        String className = classTransformData.getClassName();
        List<MethodTransformData> methodTransformations = classTransformData.getMethodTransformations();

        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation, methodTransformations);
            return;
        } catch (Exception ex) {
            System.err.println("Class [" + className + "] not found with Class.forName");
        }
        // otherwise iterate all loaded classes and find what we want
        for(Class<?> clazz: instrumentation.getAllLoadedClasses()) {
            if(clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation, methodTransformations);
                return;
            }
        }
        throw new RuntimeException("Failed to find class [" + className + "]");
    }

    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation,
                                  List<MethodTransformData> methodTransformationsSpec) {
        // Build a method transformer based on class name, class loader and method transformation specification.
        MethodTransformer mt = new MethodTransformer(clazz.getName(), classLoader, methodTransformationsSpec);
        instrumentation.addTransformer(mt, true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Transform failed for class: [" + clazz.getName() + "]", ex);
        }
    }

}
