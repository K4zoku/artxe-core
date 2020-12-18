package me.kazoku.artxe.converter.time.prototype;

import me.kazoku.artxe.converter.LinkedConverter;
import me.kazoku.artxe.converter.time.TimeConverter;
import me.kazoku.artxe.converter.time.object.BasicTimeUnit;
import me.kazoku.artxe.converter.time.object.Time;
import me.kazoku.artxe.converter.time.object.Unit;

import java.util.HashMap;
import java.util.Map;

import static me.kazoku.artxe.converter.time.object.BasicTimeUnit.*;

public class TickConverter extends TimeConverter {

    static final Map<Unit, LinkedConverter<Double>> CONVERT_TABLE;

    static {
        CONVERT_TABLE = new HashMap<>();
        // 1ms = 1/1000s
        LinkedConverter<Double> ms2t = new LinkedConverter<>(value -> value * 0.02);
        CONVERT_TABLE.put(MILLISECOND, ms2t);
        // 1µs = 1/1000ms
        LinkedConverter<Double> µ2ms = new LinkedConverter<>(value -> value / 1000, ms2t);
        CONVERT_TABLE.put(MICROSECOND, µ2ms);
        // 1ns = 1/1000µs
        LinkedConverter<Double> ns2µ = new LinkedConverter<>(value -> value / 1000, µ2ms);
        CONVERT_TABLE.put(NANOSECOND, ns2µ);
        // 1tick = 1tick
        LinkedConverter<Double> t2t = new LinkedConverter<>(value -> value);
        CONVERT_TABLE.put(TICK, t2t);
        // 1s = 20 tick
        LinkedConverter<Double> s2t = new LinkedConverter<>(value -> value * 20);
        CONVERT_TABLE.put(SECOND, s2t);
        // 1m = 60s
        LinkedConverter<Double> m2s = new LinkedConverter<>(value -> value * 60, s2t);
        CONVERT_TABLE.put(MINUTE, m2s);
        // 1h = 60m
        LinkedConverter<Double> h2m = new LinkedConverter<>(value -> value * 60, m2s);
        CONVERT_TABLE.put(HOUR, h2m);
        // 1d = 24h
        LinkedConverter<Double> d2h = new LinkedConverter<>(value -> value * 24, h2m);
        CONVERT_TABLE.put(DAY, d2h);
    }

    private static final TickConverter INSTANCE = new TickConverter();

    public static TickConverter getInstance() {
        return INSTANCE;
    }

    public TickConverter(Map<Unit, LinkedConverter<Double>> converterMap) {
        super(converterMap, BasicTimeUnit.TICK);
    }

    private TickConverter() {
        this(CONVERT_TABLE);
    }

    public static Time convertToTick(Object o) {
        return getInstance().convert(o);
    }

}
