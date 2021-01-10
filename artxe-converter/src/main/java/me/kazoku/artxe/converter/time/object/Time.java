package me.kazoku.artxe.converter.time.object;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time {

  private static final Pattern PATTERN = Pattern.compile("(?<value>\\d*[.]\\d+|\\d+)(?<unit>.*)");
  private Double value;
  private Unit unit;

  public Time(Double value, Unit unit) {
    setValue(value);
    setUnit(unit);
  }

  public Time(Double value, String unit) {
    this(value, Unit.of(unit));
  }

  public static Time parse(CharSequence text) {
    Matcher matcher = PATTERN.matcher(text);
    if (!matcher.matches()) throw new NumberFormatException();
    Double value = Double.parseDouble(matcher.group("value"));
    Unit unit = Unit.of(matcher.group("unit"));
    return new Time(value, unit);
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }

  public Time getAndUpdate(Function<Double, Double> updateFunction) {
    setValue(updateFunction.apply(getValue()));
    return this;
  }

  public Time updateUnit(Unit updater) {
    setUnit(updater);
    return this;
  }

  public String toString() {
    String val = String.valueOf(value);
    if (val.endsWith(".0")) val = String.valueOf(Math.round(value));
    return val + unit.toString();
  }
}
