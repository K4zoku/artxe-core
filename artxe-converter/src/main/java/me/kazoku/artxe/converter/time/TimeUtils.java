package me.kazoku.artxe.converter.time;

import me.kazoku.artxe.converter.time.prototype.SecondConverter;
import me.kazoku.artxe.converter.time.prototype.TickConverter;

public class TimeUtils {

  public static double toSecond(Object o) {
    return SecondConverter.convertToSecond(o).getValue();
  }

  public static long toTick(Object o) {
    return Math.round(TickConverter.convertToTick(o).getValue());
  }
}

