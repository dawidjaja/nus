import Sorters.*;
import java.util.*;

class Detective {
    private Random rng = new Random();

    // You might want this function
    // Fisher-Yates shuffle, randomly permutes an array of length n
    // in O(n) time
    private <T> void shuffle(T[] array){
        if(array==null || array.length < 2){
            return;
        }

        for(int i=1;i<array.length;i++){
            int a = rng.nextInt(i+1);
            T temp   = array[a];
            array[a] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Question 1.1
     *
     * Test whether the sorter works correctly by sorting *one* input of the
     * specified size and checking if it is sorted.
     */
    boolean checkSorted(ISort sorter, int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        shuffle(arr);
        sorter.sort(arr);
        for (int i = 0; i < size; i++) {
            if (arr[i] != i) {
                return false;
            }
        }
        return true;
    }

    /**
     * Question 1.2
     *
     * Test whether a given sort is stable by testing it on *one* input of the
     * specified size. You do not need to check if it is sorted!
     */

    private class Pair implements Comparable<Pair> {
        public int x;
        public int y;

        @Override
            public int compareTo(Pair b) {
                return this.x - b.x;
            }

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    boolean isStable(ISort sorter, int size) {
        Pair[] arr = new Pair[size];
        for (int i = 0; i < size; i++) {
            arr[i] = new Pair(i / 10, 0);
        }
        shuffle(arr);
        for (int i = 0; i < size; i++) {
            arr[i].y = i;
        }
        sorter.sort(arr);
        for (int i = 0; i < size - 1; i++) {
            if (arr[i + 1].x == arr[i].x && arr[i + 1].y <= arr[i].y) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        ISort a = new SorterC();
        if(checkSorted(a, 100)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
}
