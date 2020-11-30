package me.kazoku.artxe.bukkit.world.moon;

import org.bukkit.World;

public enum MoonPhase {
    FULL_MOON,
    WANING_GIBBOUS,
    THIRD_QUARTER,
    WANING_CRESCENT,
    NEW_MOON,
    WAXING_CRESCENT,
    FIRST_QUARTER,
    WAXING_GIBBOUS;


    MoonPhase() {}

    private static final int ONE_DAY = 24000;
    private static final int PERIOD = 8;

    public static MoonPhase getPhase(World world) {
        return values()[Math.toIntExact((world.getFullTime() / ONE_DAY) % PERIOD)];
    }
}
