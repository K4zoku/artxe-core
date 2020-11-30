package me.kazoku.artxe.configuration.path.prototype;

import me.kazoku.artxe.configuration.path.BaseConfigPath;

public final class StringConfigPath extends BaseConfigPath<String> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public StringConfigPath(String path, String def) {
    super(path, def, String::valueOf);
  }
}
