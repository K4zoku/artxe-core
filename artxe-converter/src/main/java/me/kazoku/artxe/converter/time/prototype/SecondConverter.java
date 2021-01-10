package me.kazoku.artxe.converter.time.prototype;

import me.kazoku.artxe.converter.LinkedConverter;
import me.kazoku.artxe.converter.time.TimeConverter;
import me.kazoku.artxe.converter.time.object.Time;
import me.kazoku.artxe.converter.time.object.Unit;

import java.util.HashMap;
import java.util.Map;

import static me.kazoku.artxe.converter.time.object.BasicTimeUnit.*;

public class SecondConverter extends TimeConverter {

  public static final SecondConverter INSTANCE = new SecondConverter();
  static final Map<Unit, LinkedConverter<Double>> CONVERT_TABLE;

  static {
    CONVERT_TABLE = new HashMap<>();
    // 1ms = 1/1000s
    LinkedConverter<Double> ms2s = new LinkedConverter<>(value -> value / 1000);
    CONVERT_TABLE.put(MILLISECOND, ms2s);
    // 1µs = 1/1000ms
    LinkedConverter<Double> µ2ms = new LinkedConverter<>(value -> value / 1000, ms2s);
    CONVERT_TABLE.put(MICROSECOND, µ2ms);
    // 1ns = 1/1000µs
    LinkedConverter<Double> ns2µ = new LinkedConverter<>(value -> value / 1000, µ2ms);
    CONVERT_TABLE.put(NANOSECOND, ns2µ);
    // 1tick = 1/20s
    LinkedConverter<Double> t2s = new LinkedConverter<>(value -> value / 20);
    CONVERT_TABLE.put(TICK, t2s);
    // 1s = 1s
    LinkedConverter<Double> s2s = new LinkedConverter<>(value -> value);
    CONVERT_TABLE.put(SECOND, s2s);
    // 1m = 60s
    LinkedConverter<Double> m2s = new LinkedConverter<>(value -> value * 60);
    CONVERT_TABLE.put(MINUTE, m2s);
    // 1h = 60m
    LinkedConverter<Double> h2m = new LinkedConverter<>(value -> value * 60, m2s);
    CONVERT_TABLE.put(HOUR, h2m);
    // 1d = 24h
    LinkedConverter<Double> d2h = new LinkedConverter<>(value -> value * 24, h2m);
    CONVERT_TABLE.put(DAY, d2h);
  }

  public SecondConverter(Map<Unit, LinkedConverter<Double>> converterMap) {
    super(converterMap, SECOND);
  }

  private SecondConverter() {
    this(CONVERT_TABLE);
  }

  public static TimeConverter getInstance() {
    return INSTANCE;
  }

  public static Time convertToSecond(Object o) {
    return getInstance().convert(o);
  }
}
