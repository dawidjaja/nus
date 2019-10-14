import java.io.*;
import java.math.*;
import java.util.*;

class CountingSwaps {

    // count number of swaps using divide and conquer
    private static long countSwaps(int L, int R, int[] W, int[] temp, int D) {
        // if only one element, no swaps needed
        if (L == R) return 0;

        // split array into left and right subarray
        // recurse into each subarray
        int M = (L + R) / 2;
        long swaps = 0;
        swaps += countSwaps(L, M, W, temp, D);
        swaps += countSwaps(M + 1, R, W, temp, D);

        // sort by choosing the minimum of both subarrays
        int index1 = L, index2 = M + 1, index3 = 0;
        while (index1 <= M && index2 <= R) {
            if (W[index1] <= W[index2]) {
                temp[index3++] = W[index1++];
            } else {
                // add number of swaps equal to number of elements remaining in left subarray
                // Hint: you need to modify this part to solve both parts of task B
                swaps += M - index1 + 1;
                temp[index3++] = W[index2++];
            }
        }

        // add any remaining elements in left subarray
        while (index1 <= M)
            temp[index3++] = W[index1++];

        // add any remaining elements in right subarray
        while (index2 <= R)
            temp[index3++] = W[index2++];

        // transfer elements back into original array
        System.arraycopy(temp, 0, W, L, index3);
        return swaps;
    }

    public static void main(String[] args) throws IOException {
        Reader sc = new Reader();
        int N = sc.nextInt();
        int D = sc.nextInt();
        int W[] = new int[N];
        for (int i = 0; i < N; ++i) {
            W[i] = sc.nextInt();
        }
        System.out.println(countSwaps(0, N - 1, W, new int[N], D));
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
            byte c;
            while ((c = read()) <= ' ');
            int v = 0;
            do { v = (v << 3) + (v << 1) + (c ^ '0'); }
            while ((c = read()) >= '0' && c <= '9');
            return v;
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