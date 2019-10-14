import java.util.*;

public class Main {
    public static void main(String[] args) {
        Clownculator ee = new Clownculator("../ps2_testcases/longNamesHerbert.txt");
        System.out.printf("%d\n", ee.calculateMinimumWork(47803));
        System.out.printf("%d\n", ee.calculateMinimumWork(66666));
        System.out.printf("%d\n", ee.calculateMinimumWork(13320));
        System.out.printf("%d\n", ee.calculateMinimumWork(11111));
        System.out.printf("%d\n", ee.calculateMinimumWork(111111));
        System.out.printf("%d\n", ee.calculateMinimumWork(1111111));
        System.out.printf("%d\n", ee.calculateMinimumWork(25567));
        System.out.printf("%d\n", ee.calculateMinimumWork(47804));
    }
}
