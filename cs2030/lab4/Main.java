import java.util.stream.IntStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.OptionalDouble;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        System.out.printf("Prime factors of %d are:", n);
        primeFactors(n).forEach(x -> System.out.printf(" %d", x));
        System.out.println();
    }

    static boolean isPerfect(int n) {
        if (IntStream.range(1, n)
                .filter(x -> n % x == 0)
                .sum() == n) return true;
        return false;
    }

    static boolean isSquare(int n) {
        if (n == 1) return true;
        return ((IntStream.range(1, n)
            .filter(x -> x * x == n)
            .sum()) != 0);
    }

    static int reverse(int n) {
        return IntStream.iterate(n, i -> i > 0, i -> i / 10)
                    .reduce(0, (a, b) -> (a * 10 + (b % 10)));
    }

    static int countRepeats(int[] arr) {
        return (int)(IntStream.range(0, arr.length)
            .filter(i -> (i == 0 || i == arr.length - 1) ? (i == 0) : 
                (arr[i] != arr[i - 1] || arr[i] != arr[i + 1]))
            .filter(i -> arr[i] == arr[i + 1])
            .count());
    }

    static OptionalDouble variance(int[] arr) {
        if (arr.length < 2) return OptionalDouble.empty();
        IntStream intStream = Arrays.stream(arr);
        double mean = 1.0 * Arrays.stream(arr).sum() / arr.length;
        return OptionalDouble.of(1.0 * intStream.mapToDouble(x -> ((mean - x) * (mean - x)))
                            .sum() / (arr.length - 1));
    }

    static IntStream factor(int n) {
        return (IntStream.rangeClosed(1, n)
                    .filter(x -> n % x == 0));
    }

    static IntStream primeFactors(int n) {
        return (factor(n).filter(x -> {
                    if (x == 2) return true;
                    if (x % 2 == 0) return false;
                    if (x == 1) return false;
                    for(int i = 3; i*i <= x; i += 2)
                        if (x % i == 0) return false;
                    return true;
                }));
    }
}
