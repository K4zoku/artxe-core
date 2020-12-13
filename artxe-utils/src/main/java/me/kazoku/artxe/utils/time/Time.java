package me.kazoku.artxe.utils.time;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time {

    private Double value;
    private Unit unit;

    public Time(Double value, Unit unit) {
        setValue(value);
        setUnit(unit);
    }

    public Time(Double value, String unit) {
        this(value, Unit.of(unit));
    }

    public Double getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String toString() {
        return value + unit.toString();
    }

    private static final Pattern PATTERN = Pattern.compile("(?<value>\\d*[.]\\d+|\\d+)(?<unit>.*)");

    public static Time parse(CharSequence text) {
        Matcher matcher = PATTERN.matcher(text);
        if (!matcher.matches()) throw new NumberFormatException();
        Double value = Double.parseDouble(matcher.group("value"));
        Unit unit = Unit.of(matcher.group("unit"));
        return new Time(value, unit);
    }
}
