import java.util.*;

public class Main {
    public static void main(String[] args) { 
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Point[] plist = new Point[n];
        for (int i = 0; i < n; i++) {
            plist[i] = new Point(in.nextDouble(), in.nextDouble());
            System.out.println(plist[i].toString());
        }
    }
}

