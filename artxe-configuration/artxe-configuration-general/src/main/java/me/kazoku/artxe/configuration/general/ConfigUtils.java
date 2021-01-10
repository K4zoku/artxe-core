package me.kazoku.artxe.configuration.general;

import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class ConfigUtils {
  /**
   * Class
   */
  private final Map<Class<?>, Function<Object, Object>> converterMap;

  public ConfigUtils() {
    this.converterMap = new LinkedHashMap<>();
  }


  public void convert(ConfigurationSection section) {
    section.getValues(true).forEach((key, value) ->
        converterMap.keySet().stream()
            .filter(clazz -> clazz.isInstance(value))
            .findFirst()
            .map(converterMap::get)
            .map(action -> action.apply(value))
            .ifPresent(newValue -> section.set(key, newValue))
    );
  }

  public boolean register(@NotNull Class<?> clazz, @NotNull Function<Object, Object> action) {
    if (converterMap.containsKey(clazz)) return false;
    converterMap.put(clazz, action);
    return true;
  }

  public boolean unregister(@NotNull Class<?> clazz) {
    converterMap.remove(clazz);
    return !converterMap.containsKey(clazz);
  }
}
