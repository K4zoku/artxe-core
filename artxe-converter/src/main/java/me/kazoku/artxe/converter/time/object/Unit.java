package me.kazoku.artxe.converter.time.object;

import java.util.*;

public class Unit {

    private static final Map<String, Unit> TIME_UNIT_MAP = new LinkedHashMap<>();

    private final String unit;

    private Unit(String unit) {
        this.unit = unit;
    }

    public String toString() {
        return unit;
    }

    public static Unit of(String unit) {
        return TIME_UNIT_MAP.get(unit);
    }

    public static Unit register(String unit, String... aliases) {
        Unit timeUnit = new Unit(unit);
        if (!unit.isEmpty()) TIME_UNIT_MAP.putIfAbsent(unit, timeUnit);
        for (String alias : aliases) {
            if (alias.isEmpty()) continue;
            TIME_UNIT_MAP.putIfAbsent(alias, timeUnit);
        }
        return timeUnit;
    }
}
