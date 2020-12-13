package me.kazoku.artxe.utils.time;

import java.util.function.Function;

public class FallDownConvert {
    private final boolean continueConvert;
    private final Function<Double, Double> converter;

    public FallDownConvert(boolean continueConvert, Function<Double, Double> converter) {
        this.continueConvert = continueConvert;
        this.converter = converter;
    }

    public boolean continueConvert() {
        return continueConvert;
    }

    public Double convert(Double value) {
        return converter.apply(value);
    }
}
