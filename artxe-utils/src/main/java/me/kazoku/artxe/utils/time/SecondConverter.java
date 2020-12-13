package me.kazoku.artxe.utils.time;

import me.kazoku.artxe.utils.UnitConverter;

import java.util.LinkedHashMap;

public class SecondConverter extends UnitConverter<Double> {

    private static final LinkedHashMap<String, String> UNIT_ALIASES;
    private static final LinkedHashMap<String, FallDownConvert> CONVERTER_MAP;

    static {
        UNIT_ALIASES = new LinkedHashMap<>();
        UNIT_ALIASES.put("d", "d");
        UNIT_ALIASES.put("day", "d");
        UNIT_ALIASES.put("days", "d");
        UNIT_ALIASES.put("h", "h");
        UNIT_ALIASES.put("hr", "h");
        UNIT_ALIASES.put("hrs", "h");
        UNIT_ALIASES.put("hour", "h");
        UNIT_ALIASES.put("hours", "h");
        UNIT_ALIASES.put("m", "m");
        UNIT_ALIASES.put("min", "m");
        UNIT_ALIASES.put("minute", "m");
        UNIT_ALIASES.put("minutes", "m");
        UNIT_ALIASES.put("s", "s");
        UNIT_ALIASES.put("sec", "s");
        UNIT_ALIASES.put("second", "s");
        UNIT_ALIASES.put("seconds", "s");
        UNIT_ALIASES.put("ms", "ms");
        UNIT_ALIASES.put("milli", "ms");
        UNIT_ALIASES.put("millis", "ms");
        UNIT_ALIASES.put("millisecond", "ms");
        UNIT_ALIASES.put("milliseconds", "ms");
        UNIT_ALIASES.put("µs", "µs");
        UNIT_ALIASES.put("micro", "µs");
        UNIT_ALIASES.put("micros", "µs");
        UNIT_ALIASES.put("microsecond", "µs");
        UNIT_ALIASES.put("microseconds", "µs");
        UNIT_ALIASES.put("ns", "ns");
        UNIT_ALIASES.put("nano", "ns");
        UNIT_ALIASES.put("nanos", "ns");
        UNIT_ALIASES.put("nanosecond", "ns");
        UNIT_ALIASES.put("nanoseconds", "ns");
        UNIT_ALIASES.put("ps", "ps");
        UNIT_ALIASES.put("pico", "ps");
        UNIT_ALIASES.put("picoseconds", "ps");
        UNIT_ALIASES.put("t", "t");
        UNIT_ALIASES.put("tick", "t");

        CONVERTER_MAP = new LinkedHashMap<>();
        CONVERTER_MAP.put("d", new FallDownConvert(true, l -> l * 24));
        CONVERTER_MAP.put("h", new FallDownConvert(true, l -> l * 60));
        CONVERTER_MAP.put("m", new FallDownConvert(true, l -> l * 60));
        CONVERTER_MAP.put("s", new FallDownConvert(true, l -> l));

        CONVERTER_MAP.put("t", new FallDownConvert(false, l -> l / 20));

        CONVERTER_MAP.put("ps", new FallDownConvert(true, l -> l / 1000));
        CONVERTER_MAP.put("ns", new FallDownConvert(true, l -> l / 1000));
        CONVERTER_MAP.put("µs", new FallDownConvert(true, l -> l / 1000));
        CONVERTER_MAP.put("ms", new FallDownConvert(false, l -> l / 1000));
    }

    public SecondConverter() {
        super(UNIT_ALIASES, CONVERTER_MAP);
    }

    @Override
    public Double convert(Object o) {
        return handle(o);
    }
}
