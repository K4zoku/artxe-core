package me.kazoku.artxe.utils.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface PredicatePlus<T> extends Predicate<T>, Fallbackable<Boolean>, Catcher {
  static <T> PredicatePlus<T> of(Predicate<T> predicate) {
    return predicate::test;
  }

  static <T> PredicatePlus<T> and(Predicate<T> predicate1, Predicate<T> predicate2) {
    return of(predicate1).and(predicate2);
  }

  static <T> PredicatePlus<T> negate(Predicate<T> predicate) {
    return of(predicate).negate();
  }

  static <T> PredicatePlus<T> not(Predicate<T> predicate) {
    return negate(predicate);
  }

  static <T> PredicatePlus<T> or(Predicate<T> predicate1, Predicate<T> predicate2) {
    return of(predicate1).or(predicate2);
  }

  static <T> PredicatePlus<T> xor(Predicate<T> predicate1, Predicate<T> predicate2) {
    return of(predicate1).xor(predicate2);
  }

  @Override
  default boolean test(T t) {
    try {
      return safelyTest(t);
    } catch (Throwable throwable) {
      catcher(throwable);
      return fallback();
    }
  }

  boolean safelyTest(T t) throws Throwable;

  @Override
  default Boolean fallback() {
    return false;
  }

  @NotNull
  @Override
  default PredicatePlus<T> and(@NotNull Predicate<? super T> other) {
    Objects.requireNonNull(other);
    return t -> test(t) && other.test(t);
  }

  @NotNull
  @Override
  default PredicatePlus<T> negate() {
    return t -> !test(t);
  }

  @NotNull
  @Override
  default PredicatePlus<T> or(@NotNull Predicate<? super T> other) {
    Objects.requireNonNull(other);
    return t -> test(t) || other.test(t);
  }

  default PredicatePlus<T> xor(Predicate<? super T> other) {
    Objects.requireNonNull(other);
    return t -> test(t) ^ other.test(t);
  }
}
