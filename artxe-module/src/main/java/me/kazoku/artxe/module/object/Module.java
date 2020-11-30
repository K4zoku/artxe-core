package me.kazoku.artxe.module.object;

import me.kazoku.artxe.configuration.general.Config;
import me.kazoku.artxe.module.ModuleManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The main class of the module
 */
public abstract class Module {

  /**
   * The module's class loader
   */
  private final ModuleClassLoader moduleClassLoader;

  /**
   * The module's config
   */
  private Config moduleConfig;

  /**
   * The module's data folder
   */
  private File dataFolder;

  /**
   * Create an module
   */
  public Module() {
    this.moduleClassLoader = (ModuleClassLoader) this.getClass().getClassLoader();
  }

  /**
   * Called when loading the module
   *
   * @return whether the module loaded properly
   */
  public boolean onLoad() {
    return true;
  }

  /**
   * Called when enabling the module
   */
  public void onEnable() {
    // EMPTY
  }

  /**
   * Called after all modules enabled
   */
  public void onPostEnable() {
    // EMPTY
  }

  /**
   * Called when disabling the module
   */
  public void onDisable() {
    // EMPTY
  }

  /**
   * Called when reloading
   */
  public void onReload() {
    // EMPTY
  }

  /**
   * Get the module's description
   *
   * @return the description
   */
  @NotNull
  public final ModuleInfo getInfo() {
    return this.moduleClassLoader.getModuleInfo();
  }

  /**
   * Setup the config
   */
  public final void setupConfig() {
    this.moduleConfig = this.createConfig();
  }

  /**
   * Get the config
   *
   * @return the config
   */
  @NotNull
  public final FileConfiguration getConfig() {
    return this.getModuleConfig().getConfig();
  }

  /**
   * Get the {@link Config} of the module
   *
   * @return the {@link Config}
   */
  @NotNull
  public final Config getModuleConfig() {
    if (this.moduleConfig == null) {
      this.setupConfig();
    }
    return this.moduleConfig;
  }

  /**
   * Reload the config
   */
  public final void reloadConfig() {
    this.getModuleConfig().reloadConfig();
  }

  /**
   * Save the config
   */
  public final void saveConfig() {
    this.getModuleConfig().saveConfig();
  }

  /**
   * Get the module manager
   *
   * @return the module manager
   */
  @NotNull
  public final ModuleManager getModuleManager() {
    return this.moduleClassLoader.getModuleManager();
  }

  /**
   * Get the module's folder
   *
   * @return the directory for the module
   */
  @NotNull
  public final File getDataFolder() {
    if (this.dataFolder == null) {
      this.dataFolder = new File(this.getModuleManager().getModulesDir(), this.getInfo().getName());
    }
    if (!this.dataFolder.exists()) {
      this.dataFolder.mkdirs();
    }
    return this.dataFolder;
  }

  /**
   * Copy the resource from the module's jar
   *
   * @param path    path to resource
   * @param replace whether it replaces the existed one
   */
  public final void saveResource(@NotNull("Path cannot be null or empty") final String path, final boolean replace) {
    if (path.isEmpty()) throw new IllegalArgumentException("Path cannot be null or empty");

    final String newPath = path.replace('\\', '/');
    try (final JarFile jar = new JarFile(this.moduleClassLoader.getFile())) {
      final JarEntry jarConfig = jar.getJarEntry(newPath);
      if (jarConfig != null) {
        try (final InputStream in = jar.getInputStream(jarConfig)) {
          if (in == null) {
            throw new IllegalArgumentException(
              "The embedded resource '" + newPath + "' cannot be found");
          }
          final File out = new File(this.getDataFolder(), newPath);
          out.getParentFile().mkdirs();
          if (!out.exists() || replace) {
            Files.copy(in, out.toPath(), StandardCopyOption.REPLACE_EXISTING);
          }
        }
      } else {
        throw new IllegalArgumentException("The embedded resource '" + newPath + "' cannot be found");
      }
    } catch (final IOException e) {
      this.getModuleManager().getLogger().warning("Could not load from jar file. " + newPath);
    }
  }

  /**
   * Get the resource from the module's jar
   *
   * @param path path to resource
   *
   * @return the InputStream of the resource, or null if it's not found
   */
  @Nullable
  public final InputStream getResource(@NotNull("Path cannot be null or empty") final String path) {
    if (path.isEmpty()) throw new IllegalArgumentException("Path cannot be null or empty");

    final String newPath = path.replace('\\', '/');
    try (final JarFile jar = new JarFile(this.moduleClassLoader.getFile())) {
      final JarEntry jarConfig = jar.getJarEntry(newPath);
      if (jarConfig != null) {
        try (final InputStream in = jar.getInputStream(jarConfig)) {
          return in;
        }
      }
    } catch (final IOException e) {
      this.getModuleManager().getLogger().warning("Could not load from jar file. " + newPath);
    }
    return null;
  }

  /**
   * Get the class loader
   *
   * @return the class loader
   */
  @NotNull
  protected final ModuleClassLoader getClassLoader() {
    return this.moduleClassLoader;
  }

  /**
   * Create the config
   */
  @NotNull
  protected abstract Config createConfig();
}
