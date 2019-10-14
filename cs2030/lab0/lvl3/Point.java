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

    public double getDist(Point othP) {
        return Math.sqrt(Math.pow(this.x - othP.x, 2) + Math.pow(this.y - othP.y, 2));
    }
    
    public Point getCoincideCircle(Point othP) {
        double d = Math.sqrt(1 - Math.pow(this.getDist(othP) / 2, 2));
        Point tmp = this.getMid(othP);
        return new Point(tmp.x + d * Math.cos(this.getAngle(othP) + Math.PI / 2),
                         tmp.y + d * Math.sin(this.getAngle(othP) + Math.PI / 2));
    }

    @Override
    public String toString() {
        return "(" + String.format("%.3f", this.x) + ", " + String.format("%.3f", this.y) + ")";
    }
}
