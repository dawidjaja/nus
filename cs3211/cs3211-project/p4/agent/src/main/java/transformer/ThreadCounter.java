package transformer;

public class ThreadCounter {
    public static int stoppedIBT = 0;
    public static int stoppedCT = 0;
    public static boolean stopOnceIBT() {
        stoppedIBT++;
        return stoppedIBT >= 2;
    }

    public static boolean stopOnceCT() {
        stoppedCT++;
        return stoppedCT >= 2;
    }
}
