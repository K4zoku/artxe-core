/*
 * MIT License
 *
 * Copyright (c) 2020 Shiru ka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.kazoku.artxe.module;

import me.kazoku.artxe.configuration.general.ConfigProvider;
import me.kazoku.artxe.module.object.Module;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

/**
 * A simple implementation for the module manager
 */
public final class SimpleModuleManager extends ModuleManager {

  /**
   * The file name supplier
   */
  @NotNull
  private final Supplier<String> fileName;

  /**
   * The sort and filter function
   */
  @NotNull
  private final UnaryOperator<Map<String, Module>> sortAndFilter;

  /**
   * The config provider supplier
   */
  @NotNull
  private final Supplier<ConfigProvider<?>> configProvider;

  /**
   * The on module loading predicate
   */
  @NotNull
  private final Predicate<Module> onmoduleLoading;

  /**
   * Create a simple module manager
   *
   * @param modulesDir      the module directory
   * @param logger          the logger
   * @param fileName        the file name
   * @param sortAndFilter   the sort and filter
   * @param configProvider  the config provider
   * @param onModuleLoading the on module loading
   */
  public SimpleModuleManager(@NotNull final File modulesDir, @NotNull final Logger logger,
                             @NotNull final Supplier<String> fileName,
                             @NotNull final UnaryOperator<Map<String, Module>> sortAndFilter,
                             @NotNull final Supplier<ConfigProvider<?>> configProvider,
                             @NotNull final Predicate<Module> onModuleLoading) {
    super(modulesDir, logger);
    this.fileName = fileName;
    this.sortAndFilter = sortAndFilter;
    this.configProvider = configProvider;
    this.onmoduleLoading = onModuleLoading;
  }

  /**
   * Create a simple module manager
   *
   * @param modulesDir      the module directory
   * @param logger          the logger
   * @param fileName        the file name
   * @param configProvider  the config provider
   * @param onModuleLoading the on module loading
   */
  public SimpleModuleManager(@NotNull final File modulesDir, @NotNull final Logger logger,
                             @NotNull final Supplier<String> fileName,
                             @NotNull final Supplier<ConfigProvider<?>> configProvider,
                             @NotNull final Predicate<Module> onModuleLoading) {
    this(modulesDir, logger, fileName, map -> map, configProvider, onModuleLoading);
  }

  /**
   * Create a simple module manager
   *
   * @param modulesDir     the module directory
   * @param logger         the logger
   * @param fileName       the file name
   * @param sortAndFilter  the sort and filter
   * @param configProvider the config provider
   */
  public SimpleModuleManager(@NotNull final File modulesDir, @NotNull final Logger logger,
                             @NotNull final Supplier<String> fileName,
                             @NotNull final UnaryOperator<Map<String, Module>> sortAndFilter,
                             @NotNull final Supplier<ConfigProvider<?>> configProvider) {
    this(modulesDir, logger, fileName, sortAndFilter, configProvider, module -> true);
  }

  /**
   * Create a simple module manager
   *
   * @param modulesDir     the module directory
   * @param logger         the logger
   * @param fileName       the file name
   * @param configProvider the config provider
   */
  public SimpleModuleManager(@NotNull final File modulesDir, @NotNull final Logger logger,
                             @NotNull final Supplier<String> fileName,
                             @NotNull final Supplier<ConfigProvider<?>> configProvider) {
    this(modulesDir, logger, fileName, map -> map, configProvider);
  }

  @NotNull
  @Override
  public String getModuleConfigFileName() {
    return this.fileName.get();
  }

  @NotNull
  @Override
  protected Map<String, Module> sortAndFilter(@NotNull final Map<String, Module> original) {
    return this.sortAndFilter.apply(original);
  }

  @NotNull
  @Override
  protected ConfigProvider<?> getConfigProvider() {
    return this.configProvider.get();
  }

  @Override
  protected boolean onModuleLoading(@NotNull final Module module) {
    return this.onmoduleLoading.test(module);
  }
}
