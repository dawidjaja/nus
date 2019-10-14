import java.util.*;

public class Main {
    public static void main(String[] args) { 
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Point[] plist = new Point[n];
        for (int i = 0; i < n; i++) {
            plist[i] = new Point(in.nextDouble(), in.nextDouble());
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && plist[i].getDist(plist[j]) <= 2) {
                    int ctr = 0;
                    Point tmp = plist[i].getCoincideCircle(plist[j]);
                    for (int k = 0; k < n; k++) {
                        if (tmp.getDist(plist[k]) <= 1) {
                            ctr++;
                        }
                    }
                    if (ctr > ans)
                        ans = ctr;
                }
            }
        }
        System.out.printf("Maximum Disc Coverage: %d\n", ans);
    }
}

