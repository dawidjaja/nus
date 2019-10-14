import java.util.*;
import java.lang.Math;
public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point getMid(Point othP) {
        return new Point((this.x + othP.x) / 2, (this.y + othP.y) / 2);
    }

    public double getAngle(Point othP) {
        return Math.atan2(othP.y - this.y, othP.x - this.x);
    }

    @Override
    public String toString() {
        return "(" + String.format("%.3f", this.x) + ", " + String.format("%.3f", this.y) + ")";
    }
}
