package transformer.data;

public class LineTransformData {
    private int lineNumber;
    private String code;

    public LineTransformData() {}
    public LineTransformData(int lineNumber, String code) {
        this.lineNumber = lineNumber;
        this.code = code;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return String.format("line number: %d\ncode: %s\n", this.lineNumber, this.code);
    }
}
