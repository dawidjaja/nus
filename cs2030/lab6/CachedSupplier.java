package cs2030.mystream;
import java.util.*;
import java.util.function.*;

public class CachedSupplier<T> implements Supplier<T> {
  private Supplier<T> supplier;
  private T cachedValue;
  private Boolean cached;

  public CachedSupplier(Supplier<T> supplier) {
    this.cached = false;
    this.supplier = supplier;
  }

  public CachedSupplier(T t) {
    this.cached = true;
    this.cachedValue = t;
    this.supplier = () -> t;
  }

  @Override
  public T get() {
    if (!this.cached) {
      this.cached = true;
      cachedValue = supplier.get();
    }

    return cachedValue;
  }
}

