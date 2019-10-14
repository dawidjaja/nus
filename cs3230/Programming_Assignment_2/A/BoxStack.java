import java.io.*;
import java.math.*;
import java.util.*;
import java.util.stream.Collectors;

class BoxStack {
    private static ArrayList<Integer> stackBoxes(int N, int[] W, int[] S) {
        ArrayList<Integer> finalStack = new ArrayList<>();
        for (int i = 1; i <= N; ++i) {
            // modify this logic
            finalStack.add(i);
        }
        return finalStack;
    }

    public static void main(String[] args) throws IOException {
        Reader sc = new Reader();
        int N = sc.nextInt();
        int[] W = new int[N];
        for (int i = 0; i < N; ++i) {
            W[i] = sc.nextInt();
        }
        int[] S = new int[N];
        for (int i = 0; i < N; ++i) {
            S[i] = sc.nextInt();
        }
        System.out.println(stackBoxes(N, W, S).stream().map(i->i.toString()).collect(Collectors.joining(" ")));
    }

    private static class Reader {
        final private int BUFFER_SIZE = 1 << 16;
        private DataInputStream din;
        private byte[] buffer;
        private int bufferPointer, bytesRead;

        public Reader() {
            din = new DataInputStream(System.in);
            buffer = new byte[BUFFER_SIZE];
            bufferPointer = bytesRead = 0;
        }

        public int nextInt() throws IOException {
            int ret = 0;
            byte c = read();
            while (c <= ' ') c = read();
            do {
                ret = ret * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');
            return ret;
        }

        private void fillBuffer() throws IOException {
            bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (bytesRead == -1) buffer[0] = -1;
        }

        private byte read() throws IOException {
            if (bufferPointer == bytesRead) fillBuffer();
            return buffer[bufferPointer++];
        }
    }
}