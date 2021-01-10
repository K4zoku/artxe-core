package me.kazoku.artxe.configuration.path.prototype;

import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.configuration.path.AdvancedConfigPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.serialization.ConfigurationSerializable;

public class SerializableConfigPath<T extends ConfigurationSerializable> extends AdvancedConfigPath<Object, T> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public SerializableConfigPath(@NotNull String path, @Nullable T def) {
    super(path, def);
  }

  @Override
  public @Nullable Object getFromConfig(@NotNull Config config) {
    return config.getConfig().get(path);
  }

  @Override
  @SuppressWarnings("unchecked")
  public @Nullable T convert(@NotNull Object rawValue) {
    return (T) rawValue;
  }

  @Override
  public @Nullable Object convertToRaw(@NotNull T value) {
    return value;
  }


}
