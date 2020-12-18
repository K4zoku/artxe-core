package me.kazoku.artxe.converter.time.object;

public class BasicTimeUnit {
    public static final Unit NANOSECOND = Unit.register("ns", "nano", "nanos", "nanosecond", "nanoseconds");
    public static final Unit MICROSECOND = Unit.register("µs", "micro", "microsecond", "microseconds");
    public static final Unit MILLISECOND = Unit.register("ms", "milli", "millis", "millisecond", "milliseconds");
    public static final Unit TICK = Unit.register("tick", "t");
    public static final Unit SECOND = Unit.register("s", "sec", "second", "seconds");
    public static final Unit MINUTE = Unit.register("m", "min", "minute", "minutes");
    public static final Unit HOUR = Unit.register("h", "hr", "hrs", "hour", "hours");
    public static final Unit DAY = Unit.register("d", "day", "days");
}
