import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Test {
    // Your code
    public static void main(String args[]) {
        // Your code
        int ans = 0;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(args[0]));
            int c = bf.read();
            int n = 0;
            HashMap<IntArr, Integer> hmap = new HashMap<IntArr, Integer>();
            while (48 <= c && c <= 57) {
                n = ((n << 1) + (n << 3)) + (int)c - 48;
                c = bf.read();
            }
            while (n-- > 0) {
                while (c == 13 || c == 10)
                    c = bf.read();
                int[] cnt = new int[129];
                while (c != -1 && c != 10) {
                    if (c != 13) {
                        cnt[(int)c]++;
                    }
                    c = bf.read();
                }
                IntArr ia = new IntArr(cnt);
                if (hmap.containsKey(ia)) {
                    int tmp = hmap.get(ia);
                    ans += tmp;
                    hmap.replace(ia, tmp + 1);
                } else {
                    hmap.put(ia, 1);
                }
            }
        } catch (IOException e) {
        }
        System.out.printf("%d", ans);
    }

    static class IntArr {
        final int[] arr;
        IntArr(int[] arr) {
            this.arr = arr;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.arr);
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof IntArr))
                return false;
            return Arrays.equals(this.arr, ((IntArr)o).arr);
        }
    }
}
