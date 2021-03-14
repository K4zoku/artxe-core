package me.kazoku.artxe.utils.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowableConsumer<T> extends Consumer<T> {
  static <T> ThrowableConsumer<T> of(Consumer<T> consumer) {
    return consumer::accept;
  }

  @Override
  default void accept(T t) {

  }

  void safelyAccept(T t);
}
