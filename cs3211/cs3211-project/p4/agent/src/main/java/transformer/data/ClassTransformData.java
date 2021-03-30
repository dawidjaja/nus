package transformer.data;

import java.util.List;

public class ClassTransformData {
    private String className;
    private List<MethodTransformData> methodTransformations;

    // Constructor, Getter, and Setter
    public ClassTransformData() {}
    public ClassTransformData(String className, List<MethodTransformData> methodTransformations) {
        this.className = className;
        this.methodTransformations = methodTransformations;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<MethodTransformData> getMethodTransformations() {
        return methodTransformations;
    }

    public void setMethodTransformations(List<MethodTransformData> methodTransformations) {
        this.methodTransformations = methodTransformations;
    }

    @Override
    public String toString() {
        return "Class name: " + this.className + "\nMethod transform: " + this.methodTransformations;
    }
}
