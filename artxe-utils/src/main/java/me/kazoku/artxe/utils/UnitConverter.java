package me.kazoku.artxe.utils;

import me.kazoku.artxe.utils.time.FallDownConvert;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UnitConverter<U extends Number> {

    private static final String NUMBER_PATTERN = "(?<value>\\d*[.]\\d+|\\d+)";

    private final Pattern pattern;
    private final Map<String, String> unitAliases;
    private final Map<String, FallDownConvert> converterMap;

    protected UnitConverter(Map<String, String> unitAliases, Map<String, FallDownConvert> converterMap) {
        this.unitAliases = unitAliases;
        this.converterMap = converterMap;
        String unitPattern = "(?<unit>" + String.join("|", unitAliases.keySet()) + ")?";
        pattern = Pattern.compile(NUMBER_PATTERN+unitPattern);
    }

    protected final Double handle(Object o) {
        AtomicReference<Double> result = new AtomicReference<>(-1D);
        final Matcher matcher = pattern.matcher(String.valueOf(o));
        if (matcher.matches()) {
            result.set(Double.parseDouble(matcher.group("value")));
            String unit = unitAliases.getOrDefault(matcher.group("unit"), "");
            boolean triggered = false;
            if (!unit.isEmpty()) {
                for (Map.Entry<String, FallDownConvert> converter : converterMap.entrySet()) {
                    if (!triggered && unit.equals(converter.getKey())) triggered = true;
                    if (triggered) {
                        result.getAndUpdate(converter.getValue()::convert);
                        if (!converter.getValue().continueConvert()) break;
                    }
                }
            }
        }
        return result.get();
    }

    public abstract U convert(Object o);
}
