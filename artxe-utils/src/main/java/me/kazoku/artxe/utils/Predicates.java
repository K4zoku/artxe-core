package me.kazoku.artxe.utils;

import java.util.Arrays;
import java.util.function.Predicate;

public final class Predicates {

    public static <T> Predicate<T> negate(Predicate<T> predicate) {
        return t -> !predicate.test(t);
    }

    public static <T> Predicate<T> and(Predicate<T>... predicates) {
        return t -> Arrays.stream(predicates).allMatch(p -> p.test(t));
    }

    public static <T> Predicate<T> or(Predicate<T>... predicates) {
        return t -> Arrays.stream(predicates).anyMatch(p -> p.test(t));
    }
}
