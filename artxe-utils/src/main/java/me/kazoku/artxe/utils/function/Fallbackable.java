package me.kazoku.artxe.utils.function;

public interface Fallbackable<T> {
  default T fallback() {
    return null;
  }
}
