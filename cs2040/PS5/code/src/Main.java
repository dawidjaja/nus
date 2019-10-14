import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

public class Main {
    static class IntHashMap {
        private transient Entry table[];

        private transient int count;

        private int threshold;

        private float loadFactor;

        private static class Entry {
            int hash;
            int[] key;
            int value;
            Entry next;

            protected Entry(int hash, int[] key, int value, Entry next) {
                this.hash = hash;
                this.key = key;
                this.value = value;
                this.next = next;
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(this.key);
            }

            @Override
            public boolean equals(Object o) {
                if(!(o instanceof Entry))
                    return false;
                return Arrays.equals(this.key, ((Entry)o).key);
            }
        }

        public IntHashMap() {
            this(128, 0.5f);
        }

        public IntHashMap(int initialCapacity, float loadFactor) {
            super();
            this.loadFactor = loadFactor;
            table = new Entry[initialCapacity];
            threshold = (int) (initialCapacity * loadFactor);
        }

        public static int hash(int[] key) {
            return Arrays.hashCode(key);
        }

        public int containsKey(int[] key) {
            Entry tab[] = table;
            int hash = this.hash(key);
            int index = (hash) & (tab.length - 1);
            for (Entry e = tab[index]; e != null; e = e.next) {
                if (e.hash == hash && Arrays.equals(key, e.key)) {
                    return (e.value++);
                }
            }
            /*
            for (Entry e = tab[index]; e != null; e = e.next) {
                if (e.hash == hash && Arrays.equals(key, e.key)) {
                    int old = e.value;
                    e.value = value;
                    return old;
                }
            }*/
            if (2 * count >= tab.length) {
                // Rehash the table if the threshold is exceeded
                rehash();

                tab = table;
                index = (hash) & (tab.length - 1);
            }
            // Creates the new entry.
            Entry e = new Entry(hash, key, 1, tab[index]);
            tab[index] = e;
 //           count++;
            return 0;
        }

        protected void rehash() {
            int oldCapacity = table.length;
            Entry oldMap[] = table;

            int newCapacity = oldCapacity * 2;
            Entry newMap[] = new Entry[newCapacity];

            threshold = (int) (newCapacity * loadFactor);
            table = newMap;

            for (int i = oldCapacity; i-- > 0;) {
                for (Entry old = oldMap[i]; old != null;) {
                    Entry e = old;
                    old = old.next;

                    int index = (e.hash) & (newCapacity - 1);
                    e.next = newMap[index];
                    newMap[index] = e;
                }
            }
        }
    }

    static BufferedInputStream bf;
    static int n;
    static int c;
    static IntHashMap hmap;

    static int f() {
        try {
            while (c == 13 || c == 10)
                c = bf.read();
            int[] cnt = new int[129];
            while (c != -1 && c != 13 && c != 10) {
                cnt[(int)c]++;
                c = bf.read();
            }
            int tmp = hmap.containsKey(cnt);
            return tmp;
        } catch (IOException e) {
        }
        return 0;
    }

    public static void main(String args[]){
        if(args != null && args.length > 0) {
            int ans = 0;
            try {
                bf = new BufferedInputStream(new FileInputStream(args[0]));
                c = bf.read();
                n = 0;
                while (48 <= c && c <= 57) {
                    n = ((n << 1) + (n << 3)) + (int)c - 48;
                    c = bf.read();
                }
                hmap = new IntHashMap(n, 0.75f);
                while (n-- > 0) {
                    ans += f();
                }
            } catch (IOException e) {
            }
            System.out.print(ans);
        }else{
            throw new IllegalArgumentException();
        }
    }
}
