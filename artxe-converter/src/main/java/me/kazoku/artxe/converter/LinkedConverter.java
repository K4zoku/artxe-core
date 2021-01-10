package me.kazoku.artxe.converter;

import java.util.function.Function;

public class LinkedConverter<T extends Number> implements Converter<T, T> {

  private final Function<T, T> converter;
  private final LinkedConverter<T> next;


  public LinkedConverter(Function<T, T> converter, LinkedConverter<T> next) {
    this.converter = converter;
    this.next = next;
  }

  public LinkedConverter(Function<T, T> converter) {
    this(converter, null);
  }

  public boolean hasNext() {
    return next != null;
  }

  public LinkedConverter<T> next() {
    return next;
  }

  @Override
  public T convert(T from) {
    T result = converter.apply(from);
    if (hasNext()) result = next().convert(result);
    return result;
  }
}
