package me.kazoku.artxe.configuration.path.prototype;

import me.kazoku.artxe.configuration.path.BaseConfigPath;

public final class LongConfigPath extends BaseConfigPath<Long> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public LongConfigPath(String path, Long def) {
    super(path, def, o -> {
      try {
        return Long.parseLong(String.valueOf(o));
      } catch (Exception e) {
        return def;
      }
    });
  }
}
