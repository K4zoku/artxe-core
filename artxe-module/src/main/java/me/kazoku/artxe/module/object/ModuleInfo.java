package me.kazoku.artxe.module.object;

import me.kazoku.artxe.configuration.general.ConfigProvider;
import me.kazoku.artxe.module.exception.RequiredModulePathException;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The description for the Module
 */
public final class ModuleInfo {

  /**
   * The name of the module
   */
  @NotNull
  private final String name;

  /**
   * The version of the module
   */
  @NotNull
  private final String version;

  /**
   * The main class of the module
   */
  @NotNull
  private final String mainClass;

  /**
   * The configuration of the module
   */
  @NotNull
  private final FileConfiguration configuration;

  /**
   * Create an module description
   *
   * @param name          the name of the module
   * @param version       the version of the module
   * @param mainClass     the main class of the module
   * @param configuration the configuration of the module
   */
  public ModuleInfo(@NotNull final String name, @NotNull final String version, @NotNull final String mainClass,
                    @NotNull final FileConfiguration configuration) {
    this.name = name;
    this.version = version;
    this.mainClass = mainClass;
    this.configuration = configuration;
  }

  /**
   * Generate the module description
   *
   * @param jar                  the module jar
   * @param moduleConfigFileName the name of the module config file
   * @param provider             the config provider
   * @return the module description
   * @throws IOException if there is an error when loading the module jar
   */
  @NotNull
  public static ModuleInfo get(@NotNull final JarFile jar, @NotNull final String moduleConfigFileName,
                               @NotNull final ConfigProvider<? extends FileConfiguration> provider)
      throws IOException {
    // Load the module config file
    final JarEntry entry = jar.getJarEntry(moduleConfigFileName);
    if (entry == null) {
      throw new NoSuchFileException("Module '" + jar.getName() + "' doesn't contain " + moduleConfigFileName + " file");
    }
    final InputStream inputStream = jar.getInputStream(entry);
    final FileConfiguration data = provider.loadConfiguration(inputStream);
    inputStream.close();
    // Load required descriptions
    final String name = data.getString("name");
    final String version = data.getString("version");
    final String mainClass = data.getString("main");
    if (name == null) {
      throw new RequiredModulePathException("Module '" + jar.getName() + "' doesn't have a name on " +
          moduleConfigFileName);
    }
    if (version == null) {
      throw new RequiredModulePathException("Module '" + jar.getName() + "' doesn't have a version on " +
          moduleConfigFileName);
    }
    if (mainClass == null) {
      throw new RequiredModulePathException("Module '" + jar.getName() + "' doesn't have a main class on " +
          moduleConfigFileName);
    }
    return new ModuleInfo(name, version, mainClass, data);
  }

  /**
   * Get the name of the module
   *
   * @return the name
   */
  @NotNull
  public String getName() {
    return this.name;
  }

  /**
   * Get the version of the module
   *
   * @return the version
   */
  @NotNull
  public String getVersion() {
    return this.version;
  }

  /**
   * Get the main class of the module
   *
   * @return the path to the main class
   */
  @NotNull
  public String getMainClass() {
    return this.mainClass;
  }

  /**
   * Get the module config file
   *
   * @return the file
   */
  @NotNull
  public FileConfiguration getConfiguration() {
    return this.configuration;
  }
}
