import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    // Your code
    public static void main(String args[]) {
        // Your code
        if(args != null && args.length > 0) {
            int ans = 0;
            try {
                BufferedReader bf = new BufferedReader(new FileReader(args[0]));
                int c = bf.read();
                int n = 0;
                HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();
                while (48 <= c && c <= 57) {
                    n = ((n << 1) + (n << 3)) + (int)c - 48;
                    c = bf.read();
                }
                while (n-- > 0) {
                    while (c == 13 || c == 10)
                        c = bf.read();
                    long x = 0;
                    int[] cnt = new int[129];
                    while (c != -1 && c != 13 && c != 10) {
                        if (cnt[(int)c] == null) {
                            cnt[(int)c] = 1;
                        } else {
                            cnt[(int)c]++;
                        }
                        c = bf.read();
                    }
                    Integer x = Arrays.hashCode(cnt);
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
            System.out.printf("%d", ans);
        }else{
            throw new IllegalArgumentException();
        }
    }
}

