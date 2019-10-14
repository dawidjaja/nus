import java.util.HashMap;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class MySpeedDemon {
    // Your code
    public static int speedMatch(String dbfilename) {
        // Your code
        int ans = 0;
        try {
            BufferedInputStream bf = new BufferedInputStream(new FileInputStream(dbfilename));
            int c = bf.read();
            int n = 0;
            HashMap<Long, Integer> hmap = new HashMap<Long, Integer>();
            while (48 <= c && c <= 57) {
                n = ((n << 1) + (n << 3)) + (int)c - 48;
                c = bf.read();
            }
            long[] mult = new long[128];
            mult[0] = 1;
            for (int i = 1; i < 128; i++) {
                mult[i] = mult[i - 1] * 131;
            }
            while (n-- > 0) {
                while (c == 13 || c == 10)
                    c = bf.read();
                long x = 0;
                while (c != -1 && c != 13 && c != 10) {
                    x += mult[(int)c];
                    c = bf.read();
                }
                if (hmap.containsKey(x)) {
                    int tmp = hmap.get(x);
                    ans += tmp;
                    hmap.replace(x, tmp + 1);
                } else {
                    hmap.put(x, 1);
                }
            }
        } catch (IOException e) {
        }
        return ans;
    }
}

