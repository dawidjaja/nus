import java.util.*;

public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + String.format("%.3f", this.x) + ", " + String.format("%.3f", this.y) + ")";
    }
}
