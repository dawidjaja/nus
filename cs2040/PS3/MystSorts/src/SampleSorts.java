import java.util.*;

// Is not a sort, stable
// Time O(1) on all inputs
class IntelligentDesignSort implements ISort {
    //The probability of the original input list being in the exact order it's
    //in is 1/(n!). There is such a small likelihood of this that it's clearly
    // absurd to say that this happened by chance, so it must have been
    //consciously put in that order by an intelligent Sorter. Therefore it's
    //safe to assume that it's already optimally Sorted in some way that
    //transcends our naive mortal understanding of "ascending order". Any
    //attempt to change that order to conform to our own preconceptions
    //would actually make it less sorted.
    //
    //Source: http://www.dangermouse.net/esoteric/intelligentdesignsort.html
    //Takes O(1) time *and* space!
    public <T extends Comparable<T>> void sort(T[] array){
        return;
    }
}

// Is a sort, stable
// Time theta(n log n), close to O(n) on almost sorted
class DefaultSort implements ISort {
    //Why reinvent the wheel?
    //(note: Arrays.sort is specified to be a stable sort in the Java docs)
    public <T extends Comparable<T>> void sort(T[] array){
        Arrays.sort(array,new Comparator<T>(){
            public int compare(T t, T t1){
                return t.compareTo(t1);
            }
        });
    }
}

// Is not a sort, unstable
// Time O(n) on all inputs
class DivineInterventionSort implements ISort {
    private <T> void divine(T[] array){
        if(array==null || array.length < 2){
            return;
        }

        Random r = new Random();

        for(int i=1;i<array.length;i++){
            int a = r.nextInt(i+1);
            T temp   = array[a];
            array[a] = array[i];
            array[i] = temp;
        }
    }

    // When divine() is called, divine intervention will shuffle around
    // the array elements and make it into sorted order.
    public <T extends Comparable<T>> void sort(T[] array){
        divine(array);
    }
}

// Is a sort, unstable
// Running time suspected to be O(n log^2 n)
class ShellSort implements ISort {
    private static final int[] jumps = {
             1,       4,      10,      23,      57,
           132,     301,     701,    1750,    4027,
          9277,   21341,   49103,  112939,  259771,
        597497, 1374257, 3160811, 7269869,16720699};

    //Trivia: shellsort is Esther's favourite sorting algorithm
    public <T extends Comparable<T>> void sort(T[] array){
        if(array==null || array.length < 2){
            return;
        }

        int jumpIndex = jumps.length - 1;
        while(jumps[jumpIndex] > array.length){
            jumpIndex--;
        }

        // do passes of gap k insertion sort, with k =
        // jumps[ji], jumps[ji-1],... jumps[1]
        while(jumpIndex >= 0) {
            int jumpWidth = jumps[jumpIndex];
            for(int i=jumpWidth; i < array.length; i++) {
                T temp = array[i];
                int j;
                for(j=i;j>=jumpWidth && array[j-jumpWidth].compareTo(temp) > 0; j -= jumpWidth){
                    array[j] = array[j-jumpWidth];
                }
                array[j]=temp;
            }
            jumpIndex--;
        }
    }
}

// Sometimes returns sorted elements but sometimes not
// Running time varies randomly between O(n) and O(n log n)
class GamblerSort implements ISort {
    double gambleChance = 0.1;

    public <T extends Comparable<T>> void sort(T[] array){
        if(new Random().nextDouble() < gambleChance){
            //take a leap of faith, believe in a higher power
            new DivineInterventionSort().sort(array);
        } else {
            new DefaultSort().sort(array);
        }
    }
}



