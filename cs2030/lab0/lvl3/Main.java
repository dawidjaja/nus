import java.util.*;

public class Main {
    public static void main(String[] args) { 
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Point[] plist = new Point[n];
        for (int i = 0; i < n; i++) {
            plist[i] = new Point(in.nextDouble(), in.nextDouble());
            if (i != 0 && plist[i].getDist(plist[i - 1]) < 2) {
                System.out.println(plist[i - 1].toString() + " and " + 
                        plist[i].toString() + " coincides with circle of centre " +
                        plist[i - 1].getCoincideCircle(plist[i]));
            }
        }
    }
}

