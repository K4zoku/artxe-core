package me.kazoku.artxe.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {

    private static final Pattern TIME_UNIT_MATCH = Pattern.compile("(?i)([0-9]*[.][0-9]+|[0-9]+)(tick|s|ms|m|h|d)?");

    public static long toTick(Object object) {
        String text = object.toString();
        long result = -0x1L;
        Matcher matcher = TIME_UNIT_MATCH.matcher(text);
        if (matcher.matches()) {
            double value = Double.parseDouble(matcher.group(1));
            String unit = matcher.group(2);
            result = Math.round(value);
            switch (unit) {
                // ⇓ I'm falling down! Wreeeeeeeeeeee ⇓
                case "d" : result *= 0x18;
                case "h" : result *= 0x3c;
                case "m" : result *= 0x3c;
                case "s" : result *= 0x14;
                    break;
                // Why does this exist? (ㆆ_ㆆ)
                case "ms": result *= 0x4e20;
                    break;
                default: break;
            }
        }
        return result;
    }

}
