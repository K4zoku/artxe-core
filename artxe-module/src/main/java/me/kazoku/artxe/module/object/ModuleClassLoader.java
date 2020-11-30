package me.kazoku.artxe.module.object;

import me.kazoku.artxe.module.ModuleManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * The class loader of the module
 */
public final class ModuleClassLoader extends URLClassLoader {

  /**
   * The module
   */
  @NotNull
  private final Module module;

  /**
   * The jar file of the module
   */
  @NotNull
  private final File file;

  /**
   * The module manager
   */
  @NotNull
  private final ModuleManager moduleManager;

  /**
   * The module's description
   */
  @NotNull
  private final ModuleInfo moduleInfo;

  /**
   * Create an Module Class Loader
   *
   * @param moduleManager     the module manager
   * @param file             the module jar
   * @param moduleInfo the description for the module
   * @param parent           the parent class loader
   *
   * @throws MalformedURLException     if it cannot convert the file to its related URL
   * @throws IllegalAccessException    if it cannot create an instance of the main class of the
   *                                   module
   * @throws InvocationTargetException if the constructor throws an exception
   * @throws InstantiationException    if the main class is an abstract class
   * @throws NoSuchMethodException     if it cannot find the constructor
   * @throws ClassNotFoundException    if the main class is not found
   */
  public ModuleClassLoader(@NotNull final ModuleManager moduleManager, @NotNull final File file,
                           @NotNull final ModuleInfo moduleInfo,
                           @NotNull final ClassLoader parent)
    throws MalformedURLException, IllegalAccessException, InvocationTargetException, InstantiationException,
    NoSuchMethodException, ClassNotFoundException {
    super(new URL[]{file.toURI().toURL()}, parent);
    this.moduleManager = moduleManager;
    this.file = file;
    final Class<?> clazz = Class.forName(moduleInfo.getMainClass(), true, this);
    final Class<? extends Module> newClass;
    if (Module.class.isAssignableFrom(clazz)) {
      newClass = clazz.asSubclass(Module.class);
    } else {
      throw new ClassCastException("The main class does not extend Module");
    }
    this.module = newClass.getDeclaredConstructor().newInstance();
    this.moduleInfo = moduleInfo;
  }

  /**
   * Get the module
   *
   * @return the module
   */
  @NotNull
  public Module getModule() {
    return this.module;
  }

  /**
   * Get class by the name
   *
   * @param name   the class name
   * @param global whether it'll try to search globally
   *
   * @return the class, or null if it's not found
   */
  @Nullable
  public Class<?> findClass(@NotNull final String name, final boolean global) {
    Class<?> clazz = null;
    if (global) {
      clazz = this.moduleManager.findClass(this.module, name);
    }
    if (clazz == null) {
      try {
        clazz = super.findClass(name);
      } catch (final ClassNotFoundException | NoClassDefFoundError ignored) {
        // IGNORED
      }
    }
    return clazz;
  }

  /**
   * Get the module jar
   *
   * @return the module jar
   */
  @NotNull
  public File getFile() {
    return this.file;
  }

  /**
   * Get the module manager
   *
   * @return the module manager
   */
  @NotNull
  public ModuleManager getModuleManager() {
    return this.moduleManager;
  }

  /**
   * Get the module's description
   *
   * @return the description
   */
  @NotNull
  public ModuleInfo getModuleInfo() {
    return this.moduleInfo;
  }

  @Override
  @Nullable
  protected Class<?> findClass(@NotNull final String name) {
    return this.findClass(name, true);
  }
}
