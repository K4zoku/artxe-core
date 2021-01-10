package me.kazoku.artxe.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EventClassValidate {
  // _(:3 」∠)_
  private static final Map<Class<?>, EventClassValidate> lazyMap = new HashMap<>();
  private final Class<?> eventClass;
  private Boolean lazyResult;

  private EventClassValidate(Class<?> eventClass) {
    this.eventClass = eventClass;
  }

  public static boolean test(@Nullable Class<?> rawClass) {
    return rawClass != null && lazyMap.computeIfAbsent(rawClass, EventClassValidate::new).test();
  }

  private boolean isExtendsEvent() {
    /*
     * public class <?> extends <? extends Event> {}
     */
    for (
        Class<?> current = eventClass.getSuperclass();
        !Object.class.equals(current.getSuperclass());
        current = current.getSuperclass()
    )
      if (Event.class.equals(current.getSuperclass())) return true;
    return false;
  }

  private boolean hasValidMethod() {
    /*
     * public class <?> {
     *     ...
     *     public static HandlerList getHandlerList() {}
     *     ...
     * }
     */
    return Arrays.stream(eventClass.getDeclaredMethods())
        .anyMatch(method -> {
          int modifiers = method.getModifiers();
          return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) &&
              HandlerList.class.equals(method.getReturnType()) &&
              "getHandlerList".equals(method.getName());
        });
  }

  private boolean test() {
    return (lazyResult == null) ? (lazyResult = isExtendsEvent() && hasValidMethod()) : lazyResult;
  }

}
