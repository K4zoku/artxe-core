package me.kazoku.artxe.module;

import me.kazoku.artxe.configuration.general.ConfigProvider;
import me.kazoku.artxe.module.object.Module;
import me.kazoku.artxe.module.object.ModuleClassLoader;
import me.kazoku.artxe.module.object.ModuleInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that manages all modules in it
 */
public abstract class ModuleManager {

  /**
   * The module map keyed module's id, valued module itself
   */
  private final Map<String, Module> modulesMap = new LinkedHashMap<>();

  /**
   * The module map keyed module itself, valued module's class loader
   */
  private final Map<Module, ModuleClassLoader> loaderMap = new HashMap<>();

  /**
   * The file that contains all modules
   */
  @NotNull
  private final File modulesDir;

  /**
   * The logger to use in all modules
   */
  @NotNull
  private final Logger logger;

  /**
   * Create a new module manager
   *
   * @param modulesDir the directory to store module files
   * @param logger     the logger to use in every module
   */
  protected ModuleManager(@NotNull final File modulesDir, @NotNull final Logger logger) {
    this.logger = logger;
    this.modulesDir = modulesDir;
    if (!modulesDir.exists()) {
      modulesDir.mkdirs();
    }
  }

  /**
   * Get the module directory
   *
   * @return the directory
   */
  @NotNull
  public final File getModulesDir() {
    return this.modulesDir;
  }

  /**
   * Load all modules from the module directory. Also call {@link Module#onLoad()}
   */
  public final void loadModules() {
    final Map<String, Module> moduleMap = new HashMap<>();
    // Load the module files
    Arrays.stream(Objects.requireNonNull(this.modulesDir.listFiles()))
        .filter(file -> file.isFile() && file.getName().endsWith(".jar"))
        .forEach(file -> {
          try (final JarFile jar = new JarFile(file)) {
            // Get module description
            final ModuleInfo moduleInfo = ModuleInfo.get(jar, this.getModuleConfigFileName(),
                this.getConfigProvider());
            if (moduleMap.containsKey(moduleInfo.getName())) {
              this.logger.warning("Duplicated module " + moduleInfo.getName());
              return;
            }
            // Try to load the module
            final ModuleClassLoader loader = new ModuleClassLoader(this, file, moduleInfo,
                this.getClass().getClassLoader());
            final Module module = loader.getModule();
            if (this.onModuleLoading(module)) {
              moduleMap.put(moduleInfo.getName(), loader.getModule());
              this.loaderMap.put(module, loader);
            } else {
              loader.close();
            }
          } catch (final Exception e) {
            this.logger.log(Level.WARNING, "Error when loading jar", e);
          }
        });
    // Filter and sort the modules
    final Map<String, Module> sortedModuleMap = this.sortAndFilter(moduleMap);
    // Close ModuleClassLoader of remaining modules
    moduleMap.entrySet().stream()
        .filter(entry -> !sortedModuleMap.containsKey(entry.getKey()))
        .forEach(entry -> this.closeClassLoader(entry.getValue()));
    // Load the modules
    final Map<String, Module> finalModules = new LinkedHashMap<>();
    sortedModuleMap.forEach((key, module) -> {
      try {
        if (!module.onLoad()) {
          this.logger.warning("Failed to load " + key + " " + module.getInfo().getVersion());
          this.closeClassLoader(module);
          return;
        }
        this.logger.info("Loaded " + key + " " + module.getInfo().getVersion());
        finalModules.put(key, module);
      } catch (final Throwable t) {
        this.logger.log(Level.WARNING, t, () -> "Error when loading " + key);
        this.closeClassLoader(module);
      }
    });
    // Store the final modules map
    this.modulesMap.putAll(finalModules);
  }

  /**
   * Enable (call {@link Module#onEnable()}) the module
   *
   * @param name                the module name
   * @param closeLoaderOnFailed close the class loader if failed
   * @return whether it's enabled successfully
   */
  public final boolean enableModule(@NotNull final String name, final boolean closeLoaderOnFailed) {
    final Module module = this.modulesMap.get(name);
    try {
      module.onEnable();
      return true;
    } catch (final Throwable t) {
      this.logger.log(Level.WARNING, t, () -> "Error when enabling " + name);
      if (closeLoaderOnFailed) {
        this.closeClassLoader(module);
      }
      return false;
    }
  }

  /**
   * Disable (call {@link Module#onDisable()}) the module
   *
   * @param name                the module name
   * @param closeLoaderOnFailed close the class loader if failed
   * @return whether it's disabled successfully
   */
  public final boolean disableModule(@NotNull final String name, final boolean closeLoaderOnFailed) {
    final Module module = this.modulesMap.get(name);
    try {
      module.onDisable();
      return true;
    } catch (final Throwable t) {
      this.logger.log(Level.WARNING, t, () -> "Error when disabling " + name);
      if (closeLoaderOnFailed) {
        this.closeClassLoader(module);
      }
      return false;
    }
  }

  /**
   * Enable all modules from the module directory
   */
  public final void enableModules() {
    final List<String> failed = new ArrayList<>();
    this.modulesMap.keySet().forEach(name -> {
      if (!this.enableModule(name, true)) {
        failed.add(name);
      } else {
        this.logger.log(Level.INFO, "Enabled {0}",
            String.join(" ", name, this.modulesMap.get(name).getInfo().getVersion()));
      }
    });
    failed.forEach(this.modulesMap::remove);
  }

  /**
   * Call the {@link Module#onPostEnable()} method of all enabled modules
   */
  public final void callPostEnable() {
    this.modulesMap.values().forEach(Module::onPostEnable);
  }

  /**
   * Call the {@link Module#onReload()} method of all enabled modules
   */
  public final void callReload() {
    this.modulesMap.values().forEach(Module::onReload);
  }

  /**
   * Disable all enabled modules
   */
  public final void disableModules() {
    this.modulesMap.keySet().forEach(name -> {
      if (this.disableModule(name, false)) {
        this.logger.log(Level.INFO, "Disabled {0}",
            String.join(" ", name, this.modulesMap.get(name).getInfo().getVersion()));
      }
    });
    this.modulesMap.values().forEach(this::closeClassLoader);
    this.modulesMap.clear();
  }

  /**
   * Get the enabled module
   *
   * @param name the name of the module
   * @return the module, or null if it's not found
   */
  @Nullable
  public final Module getModule(@NotNull final String name) {
    return this.modulesMap.get(name);
  }

  /**
   * Check if the module is loaded
   *
   * @param name the name of the module
   * @return whether it's loaded
   */
  public final boolean isModuleLoaded(@NotNull final String name) {
    return this.modulesMap.containsKey(name);
  }

  /**
   * Get all loaded modules
   *
   * @return the loaded modules
   */
  @NotNull
  public final Map<String, Module> getLoadedModules() {
    return Collections.unmodifiableMap(this.modulesMap);
  }

  /**
   * Get the name of the module config file
   *
   * @return the file name
   */
  @NotNull
  public abstract String getModuleConfigFileName();

  /**
   * Find a class for an module
   *
   * @param module the calling module
   * @param name   the class name
   * @return the class, or null if it's not found
   */
  @Nullable
  public final Class<?> findClass(@NotNull final Module module, @NotNull final String name) {
    for (final Map.Entry<Module, ModuleClassLoader> entry : this.loaderMap.entrySet()) {
      if (entry.getKey().equals(module)) continue;
      final Class<?> clazz = entry.getValue().findClass(name, false);
      if (clazz != null) {
        return clazz;
      }
    }
    return null;
  }

  /**
   * Get the logger
   *
   * @return the logger
   */
  @NotNull
  public final Logger getLogger() {
    return this.logger;
  }

  /**
   * Filter and sort the order of the modules
   *
   * @param original the original map
   * @return the sorted and filtered map
   */
  @NotNull
  protected Map<String, Module> sortAndFilter(@NotNull final Map<String, Module> original) {
    return original;
  }

  /**
   * Get the module config provider
   *
   * @return the provider
   */
  @NotNull
  protected abstract ConfigProvider<?> getConfigProvider();

  /**
   * Called when the module is on loading
   *
   * @param module the loading module
   * @return whether the module is properly loaded
   */
  protected boolean onModuleLoading(@NotNull final Module module) {
    return true;
  }

  /**
   * Close the class loader of the module
   *
   * @param module the module
   */
  private void closeClassLoader(@NotNull final Module module) {
    if (this.loaderMap.containsKey(module)) {
      final ModuleClassLoader loader = this.loaderMap.remove(module);
      try {
        loader.close();
      } catch (final IOException e) {
        this.logger.log(Level.WARNING, "Error when closing ClassLoader", e);
      }
    }
  }
}
