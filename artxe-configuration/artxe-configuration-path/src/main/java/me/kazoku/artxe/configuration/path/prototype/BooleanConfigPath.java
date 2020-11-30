package me.kazoku.artxe.configuration.path.prototype;

import me.kazoku.artxe.configuration.path.BaseConfigPath;

public final class BooleanConfigPath extends BaseConfigPath<Boolean> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public BooleanConfigPath(String path, Boolean def) {
    super(path, def, o -> Boolean.parseBoolean(String.valueOf(o)));
  }
}
