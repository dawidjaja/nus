package transformer;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import transformer.data.LineTransformData;
import transformer.data.MethodTransformData;

public class MethodTransformer implements ClassFileTransformer {
    /** The internal form class name of the class to transform */
    private String targetClassName;
    /** The class loader of the class we want to transform */
    private ClassLoader targetClassLoader;
    /** The transformations specification for each method */
    private List<MethodTransformData> methodTransformations;

    public MethodTransformer(String targetClassName, ClassLoader targetClassLoader,
                             List<MethodTransformData> methodTransformations) {
        this.targetClassName = targetClassName;
        this.targetClassLoader = targetClassLoader;
        this.methodTransformations = methodTransformations;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        byte[] byteCode = classfileBuffer;

        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/"); //replace . with /
        // If it is not the target class, do not modify the byte code.
        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (loader.equals(targetClassLoader)) {
            try {
                ClassPool cp = ClassPool.getDefault();
                cp.appendClassPath(new LoaderClassPath(loader));
                CtClass cc = cp.get(this.targetClassName);

                for (MethodTransformData methodTransformation : this.methodTransformations) {
                    transformMethodBasedOnSpec(cc, methodTransformation);
                }

                byteCode = cc.toBytecode();
                cc.detach();
            } catch (NotFoundException | CannotCompileException | IOException e) {
                System.out.println("Exception:\n" +  e);
            }
        }
        return byteCode;
    }

    private void transformMethodBasedOnSpec(CtClass cc, MethodTransformData methodTransformation)
            throws NotFoundException, CannotCompileException {
        // Read methodName and specific line transformations
        String methodName = methodTransformation.getMethodName();
        List<LineTransformData> lineTransformations = methodTransformation.getLineTransforms();
        String replacementCode = methodTransformation.getReplacementCode();

        CtMethod m = cc.getDeclaredMethod(methodName);

        if (lineTransformations != null) { // insert lines
            for (LineTransformData lineTransformation : lineTransformations) {
                int lineNumber = lineTransformation.getLineNumber();
                String code = lineTransformation.getCode();
                // insert the code at specified line number
                m.insertAt(lineNumber, code);
            }
        } else { // replace method body
            m.setBody(replacementCode);
        }
    }
}
