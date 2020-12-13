package me.kazoku.artxe.converter;

public interface Converter<F, T> {
    T convert(F from);
}
