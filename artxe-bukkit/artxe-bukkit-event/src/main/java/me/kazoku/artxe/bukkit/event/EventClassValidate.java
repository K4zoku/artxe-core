package me.kazoku.artxe.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EventClassValidate {
  // _(:3 」∠)_
  private static final Map<Class<?>, EventClassValidate> lazyMap = new HashMap<>();
  private final Class<?> eventClass;
  private Boolean lazyResult = null;

  private EventClassValidate(Class<?> eventClass) {
    this.eventClass = eventClass;
  }

  public static boolean test(@Nullable Class<?> rawClass) {
    return rawClass != null && lazyMap.computeIfAbsent(rawClass, EventClassValidate::new).test();
  }

  private boolean isExtendsEvent() {
    for (Class<?> current = eventClass; Event.class.equals(current); current = current.getSuperclass())
      if (Object.class.equals(current.getSuperclass())) return false;
    return true;
  }

  private boolean hasValidMethod() {
    for (Method method : eventClass.getDeclaredMethods()) {
      if (
          Modifier.isStatic(method.getModifiers()) &&
              "getHandlerList".equals(method.getName()) &&
              HandlerList.class.equals(method.getReturnType())
      ) return true;
    }
    return false;
  }

  private boolean test() {
    return Optional.ofNullable(lazyResult).orElse(lazyResult = isExtendsEvent() && hasValidMethod());
  }

}
