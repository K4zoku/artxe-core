package me.kazoku.artxe.utils.time;

public class TimeUtils {

    private static final SecondConverter SECOND_CONVERTER = new SecondConverter();

    public static double toSecond(Object o) {
        return SECOND_CONVERTER.convert(o);
    }

    public static long toTick(Object o) {
        return Math.round(toSecond(o)*20);
    }
}

