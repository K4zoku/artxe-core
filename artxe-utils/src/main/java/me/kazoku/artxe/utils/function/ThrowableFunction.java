package me.kazoku.artxe.utils.function;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowableFunction<T, R> extends Function<T, R>, Fallbackable<R>, Catcher {
  static <T, R> ThrowableFunction<T, R> of(Function<T, R> function) {
    return function::apply;
  }

  @Override
  default R apply(T t) {
    try {
      return safelyApply(t);
    } catch (Throwable throwable) {
      catcher(throwable);
      return fallback();
    }
  }

  R safelyApply(T t) throws Throwable;
}
