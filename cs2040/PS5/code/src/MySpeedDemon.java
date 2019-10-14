import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MySpeedDemon {
    static class Node {
        HashMap<Integer,Node> child;
        int value;

        public Node() {
            child = new HashMap<>();
            value = 0;
        }

        public Node add(int x) {
            if (!child.containsKey(x))
                child.put(x, new Node());
            return child.get(x);
        }

        public int inc() {
            value++;
            return value - 1;
        }
    }

    public static int speedMatch(String dbfilename) {
        int ans = 0;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(dbfilename));
            int c = bf.read();
            int n = 0;
            while (48 <= c && c <= 57) {
                n = ((n << 1) + (n << 3)) + (int)c - 48;
                c = bf.read();
            }
            Node root = new Node();
            while (n-- > 0) {
                while (c == 13 || c == 10)
                    c = bf.read();
                int[] cnt = new int[129];
                Node node = root;
                while (c != -1 && c != 13 && c != 10) {
                    cnt[(int)c]++;
                    c = bf.read();
                }
                for (int i = 0; i < 129; i++) {
                    node = node.add(cnt[i]);
                }
                ans += node.inc();
            }
        } catch (IOException e) {
        }
        return ans;
    }
}

