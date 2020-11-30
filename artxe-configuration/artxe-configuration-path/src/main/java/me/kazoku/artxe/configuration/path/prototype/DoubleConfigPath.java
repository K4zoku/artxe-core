package me.kazoku.artxe.configuration.path.prototype;

import me.kazoku.artxe.configuration.path.BaseConfigPath;

public final class DoubleConfigPath extends BaseConfigPath<Double> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public DoubleConfigPath(String path, Double def) {
    super(path, def, o -> {
      try {
        return Double.parseDouble(String.valueOf(o));
      } catch (Exception e) {
        return def;
      }
    });
  }
}
