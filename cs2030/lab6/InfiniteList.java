package cs2030.mystream;
import java.util.*;
import java.util.function.*;

@SuppressWarnings("unchecked")
public interface InfiniteList<T> {
  public static <T> InfiniteList<T> generate(Supplier<T> supp) {
    return IFL.generate(supp);
  }

  public static <T> InfiniteList<T> iterate(T seed, UnaryOperator<T> next) {
    return IFL.iterate(seed, next);
  }

  public long count();
  public void forEach(Consumer<? super T> action);
  public Optional reduce(BinaryOperator<T> accumulator);
  public T reduce(T identity, BinaryOperator<T> accumulator);
  public Object[] toArray();

  public InfiniteList<T> limit(long maxSize);
  public InfiniteList<T> filter(Predicate<? super T> predicate);
  public <R> InfiniteList<R> map(Function<? super T, ? extends R> mapper);
  public InfiniteList<T> takeWhile(Predicate<? super T> predicate);
}

@SuppressWarnings("unchecked")
class IFL<T> implements InfiniteList<T> {
  public IFL() {}

  boolean isEmpty() {
    return this.head() == null;
  }

  Supplier<T> headSupplier;
  T headCache;
  Supplier<IFL<T>> tailSupplier;
  IFL<T> tailCache;
  boolean headCached = false;
  boolean tailCached = false;

  T head() {
    if (headCached == false) {
      headCached = true;
      return headCache = headSupplier.get();
    }
    return headCache;
  }

  IFL<T> tail() {
    if (tailCached == false) {
      tailCached = true;
      return tailCache = tailSupplier.get();
    }
    return tailCache;
  }

  public IFL(T t, Supplier<IFL<T>> next) {
    this.headSupplier = () -> t;
    this.headCached = true;
    this.headCache = t;
    this.tailSupplier = next;
  }

  public IFL(Supplier<T> s, Supplier<IFL<T>> next) {
    this.headSupplier = s;
    this.tailSupplier = next;
  }

  static <T> IFL<T> generate(Supplier<T> supplier) {
    return new IFL<T>(supplier, () -> IFL.generate(supplier));
  }

  private static <T> IFL<T> supplierIterate(CachedSupplier<T> supplier, Function<T,T> next) {
    CachedSupplier<T> nextSupplier = new CachedSupplier<>(() -> next.apply(supplier.get()));
    return new IFL<T>(supplier, () -> IFL.supplierIterate(nextSupplier, next));
    // return new IFL<T>(supplier, () -> IFL.supplierIterate(new CachedSupplier<T>(() -> next.apply(supplier.get()))), next);
  }

  static <T> IFL<T> iterate(T seed, Function<T,T> next) {
    return IFL.supplierIterate(new CachedSupplier<T>(seed), next);
  } 

  @Override
  public long count() {
    if (this.isEmpty()) {
      return 0;
    }
    return 1 + this.tail().count();
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    if (!this.isEmpty()) {
      action.accept(this.head());
      this.tail().forEach(action);
    }
  }

  @Override
  public Optional reduce(BinaryOperator<T> accumulator) {
    if (this.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(this.tail().reduce(head(), accumulator));
  }

  @Override
  public T reduce(T identity, BinaryOperator<T> accumulator) {
    if (this.isEmpty()) {
      return identity;
    }
    return this.tail().reduce(accumulator.apply(identity, head()), accumulator);
  }

  @Override
  public Object[] toArray() {
    ArrayList<T> alist = new ArrayList<>();
    IFL<T> tmp = this;
    while (!tmp.isEmpty()) {
      alist.add(tmp.head());
      tmp = tmp.tail();
    }
    return alist.toArray();
  }

  @Override
  public IFL<T> limit(long maxSize) {
    if (maxSize == 0) {
      return new IFL(() -> null, () -> new EmptyList());
    }
    return new IFL(() -> this.head(), () -> this.tail().limit(maxSize - 1));
  }

  private IFL<T> findNext(Predicate<? super T> pred) {
    if (this.isEmpty()) {
      return new IFL(() -> null, () -> new EmptyList());
    }
    if (pred.test(this.head())) {
      return this;
    }
    return tail().findNext(pred);
  }

  @Override
  public IFL<T> filter(Predicate<? super T> pred) {
    CachedSupplier<IFL<T>> cachedSupplier =
      new CachedSupplier<>(() -> this.findNext(pred));
    return new IFL(() -> cachedSupplier.get().head(),
        () -> cachedSupplier.get().tail().filter(pred));
  }
  @Override
  public <R> IFL<R> map(Function<? super T, ? extends R> mapper) {
    return new IFL(
        () -> this.isEmpty() ? null : mapper.apply(head()), 
        () -> this.isEmpty() ? null : this.tail().map(mapper));
  }

  private IFL<T> takeWhile(Predicate<? super T> pred, Supplier<Boolean> isEmpty) {
    CachedSupplier<T> cachedSupplier = 
      new CachedSupplier<>(() -> this.isEmpty() || isEmpty.get() || !pred.test(this.head()) ? null : this.head());
    return new IFL(cachedSupplier,
        () -> this.tail().takeWhile(pred, () -> cachedSupplier.get() == null));
  }

  @Override
  public IFL<T> takeWhile(Predicate<? super T> pred) {
    CachedSupplier<T> cachedSupplier = 
      new CachedSupplier<>(() -> !this.isEmpty() && pred.test(this.head()) ? this.head() : null);
    return new IFL(cachedSupplier,
        () -> this.tail().takeWhile(pred, () -> cachedSupplier.get() == null));
  }
}

@SuppressWarnings("unchecked")
class EmptyList<T> extends IFL<T> {
  EmptyList(){}
  T head() {
    return null;
  }
  @Override
  public T reduce(T identity, BinaryOperator<T> accumulator) {
    return identity;
  }

  @Override
  boolean isEmpty() {
    return true;
  }
  @Override
  IFL<T> tail() {
    return new EmptyList();
  }
}
