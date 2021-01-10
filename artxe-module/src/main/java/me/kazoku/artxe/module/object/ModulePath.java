package me.kazoku.artxe.module.object;

import me.kazoku.artxe.module.exception.RequiredModulePathException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

/**
 * A path to get value from the module config file
 *
 * @param <T> the type of the final value
 */
public abstract class ModulePath<T> {

  /**
   * The path to get the value
   */
  @NotNull
  private final String path;

  /**
   * Is the path required or not
   */
  private final boolean required;

  /**
   * Create an module path
   *
   * @param path     the path to the value
   * @param required is it required to be in the module config file
   */
  public ModulePath(@NotNull final String path, final boolean required) {
    this.path = path;
    this.required = required;
  }

  /**
   * Check if the path is required to be in the module config file
   *
   * @return is it required to be in the module config file
   */
  public final boolean isRequired() {
    return this.required;
  }

  /**
   * Get the path to the value
   *
   * @return the path
   */
  @NotNull
  public final String getPath() {
    return this.path;
  }

  /**
   * Convert the type of the value from the module config file
   *
   * @param object the raw value from the module config file
   * @return the converted value
   */
  @Nullable
  public abstract T convertType(@NotNull Object object);

  /**
   * Get the value from the module config file
   *
   * @param module the module
   * @return the value
   * @throws RequiredModulePathException if the path is required but is not found in the module config file
   */
  @Nullable
  public final T get(@NotNull final Module module) {
    final FileConfiguration configuration = module.getInfo().getConfiguration();
    if (this.required && !configuration.isSet(this.path)) {
      throw new RequiredModulePathException(
          this.path + " is not found in the module '" + module.getInfo().getName() + "'");
    }
    final Object value = configuration.get(this.path);
    return value != null ? this.convertType(value) : null;
  }
}
