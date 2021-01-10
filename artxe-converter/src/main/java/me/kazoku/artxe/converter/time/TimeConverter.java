package me.kazoku.artxe.converter.time;

import me.kazoku.artxe.converter.LinkedConverter;
import me.kazoku.artxe.converter.time.object.Time;
import me.kazoku.artxe.converter.time.object.Unit;

import java.util.Map;
import java.util.Optional;

public abstract class TimeConverter {

  protected final Map<Unit, LinkedConverter<Double>> converterMap;
  protected final Unit resultUnit;

  protected TimeConverter(Map<Unit, LinkedConverter<Double>> converterMap, Unit resultUnit) {
    this.converterMap = converterMap;
    this.resultUnit = resultUnit;
  }

  protected final Time handle(Object o) {
    final Time time = Time.parse(String.valueOf(o));
    final Unit unit = time.getUnit();
    if (unit == null) return time;
    return Optional.ofNullable(converterMap.get(unit))
        .map(converter -> time.getAndUpdate(converter::convert))
        .orElse(time);
  }

  public Time convert(Object o) {
    return handle(o).updateUnit(resultUnit);
  }
}
