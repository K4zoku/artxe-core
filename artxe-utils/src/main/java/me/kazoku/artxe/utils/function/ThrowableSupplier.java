package me.kazoku.artxe.utils.function;

import java.util.Optional;
import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowableSupplier<T> extends Supplier<T>, Fallbackable<T>, Catcher {
  static <T> ThrowableSupplier<T> of(Supplier<T> supplier) {
    return supplier::get;
  }

  static <T> ThrowableSupplier<T> lambda(ThrowableSupplier<T> supplier) {
    return supplier;
  }

  @Override
  default T get() {
    try {
      return safelyGet();
    } catch (Throwable throwable) {
      catcher(throwable);
      return fallback();
    }
  }

  default Optional<T> getOptional() {
    return Optional.ofNullable(get());
  }

  T safelyGet() throws Throwable;
}
