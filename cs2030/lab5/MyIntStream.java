package cs2030.mystream;
import java.util.*;
import java.util.function.*;

public interface MyIntStream {
    public static MyIntStream of(int... arr) {
        ArrayList<Integer> tmp = new ArrayList<>();
        for (int i : arr) {
            tmp.add(i);
        }
        return new IS(tmp);
    }

    public static MyIntStream range(int start, int end) {
        return new IS(start, end);
    }

    public static MyIntStream rangeClosed(int start, int end) {
        return new IS(start, end + 1);
    }

    public int count();
    public int sum();
    public OptionalDouble average();
    public OptionalInt max();
    public OptionalInt min();
    public void forEach(IntConsumer action);

    public MyIntStream limit(int maxSize);
    public MyIntStream distinct();
    public MyIntStream filter(IntPredicate predicate);
    public MyIntStream map(IntUnaryOperator mapper);
    
    public int reduce(int identity, IntBinaryOperator op);
    public OptionalInt reduce(IntBinaryOperator op);
}

class IS implements MyIntStream {
    ArrayList<Integer> alist;
    public IS(ArrayList<Integer> arr) {
        alist = arr;
    }

    public IS(int start, int end) {
        alist = new ArrayList<Integer>();
        for (int i = start; i < end; i++) {
            alist.add(i);
        }
    }

    @Override
    public int count() {
        return alist.size();
    }

    @Override
    public int sum() {
        int ttl = 0;
        for (int i : alist) {
            ttl += i;
        }
        return ttl;
    }

    @Override
    public OptionalDouble average() {
        if (alist.size() == 0) return OptionalDouble.empty();
        double ttl = this.sum();
        return OptionalDouble.of(ttl / this.count());
    }

    @Override
    public OptionalInt max() {
        if (alist.size() == 0) return OptionalInt.empty();
        int maxValue = alist.get(0);
        for (int i : alist) {
            if (i > maxValue) maxValue = i;
        }
        return OptionalInt.of(maxValue);
    }

    @Override
    public OptionalInt min() {
        if (alist.size() == 0) return OptionalInt.empty();
        int minValue = alist.get(0);
        for (int i : alist) {
            if (i < minValue) minValue = i;
        }
        return OptionalInt.of(minValue);
    }

    @Override
    public void forEach(IntConsumer action) {
        for (int i : alist) {
            action.accept(i);
        }
    }

    @Override
    public IS limit(int maxSize) {
        ArrayList<Integer> tmp = new ArrayList<>();
        for (int i = 0; i < Math.min(maxSize, alist.size()); i++) {
            tmp.add(alist.get(i));
        }
        return new IS(tmp);
    }

    @Override
    public IS distinct() {
        ArrayList<Integer> tmp = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        for (int i : alist) {
            if (!set.contains(i)) {
                tmp.add(i);
                set.add(i);
            }
        }
        return new IS(tmp);
    }

    @Override
    public IS filter(IntPredicate predicate) {
        ArrayList<Integer> tmp = new ArrayList<>();
        for (int i : alist) {
            if (predicate.test(i)) {
                tmp.add(i);
            }
        }
        return new IS(tmp);
    }

    @Override
    public IS map(IntUnaryOperator mapper) {
        ArrayList<Integer> tmp = new ArrayList<>();
        for (int i : alist) {
            tmp.add(mapper.applyAsInt(i));
        }
        return new IS(tmp);
    }
    
    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        int x = identity;
        for (int i : alist) {
            x = op.applyAsInt(x, i);
        }
        return x;
    }

    @Override
    public OptionalInt reduce(IntBinaryOperator op) {
        if (alist.size() == 0) {
            return OptionalInt.empty();
        }
        int x = alist.get(0);
        for (int i = 1; i < alist.size(); i++) {
            x = op.applyAsInt(x, alist.get(i));
        }
        return OptionalInt.of(x);
    }
}
