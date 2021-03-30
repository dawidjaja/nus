package transformer.data;

import java.util.List;

public class MethodTransformData {
    private String methodName;
    private List<LineTransformData> lineTransforms;
    private String replacementCode;

    // Constructor, Getter, and Setter
    public MethodTransformData() {}
    public MethodTransformData(String methodName, List<LineTransformData> lineTransforms, String replacementCode) {
        this.methodName = methodName;
        this.lineTransforms = lineTransforms;
        this.replacementCode = replacementCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<LineTransformData> getLineTransforms() {
        return lineTransforms;
    }

    public void setLineTransforms(List<LineTransformData> lineTransforms) {
        this.lineTransforms = lineTransforms;
    }

    @Override
    public String toString() {
        return "method name: " + this.methodName +
                "\nspecific lines: " + this.lineTransforms +
                "\breplacement code : " + this.replacementCode + "\n";
    }

    public String getReplacementCode() {
        return replacementCode;
    }

    public void setReplacementCode(String replacementCode) {
        this.replacementCode = replacementCode;
    }
}
