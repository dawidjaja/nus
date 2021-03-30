package transformer.data;

import java.util.List;

public class TransformData {
    private List<ClassTransformData> transformations;

    public TransformData() {}
    public TransformData(List<ClassTransformData> transformations) {
        this.transformations = transformations;
    }

    public List<ClassTransformData> getTransformations() {
        return transformations;
    }

    public void setTransformations(List<ClassTransformData> transformations) {
        this.transformations = transformations;
    }

    @Override
    public String toString() {
        return "Transformations: " + this.transformations + "\n";
    }
}
