package me.kazoku.artxe.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Consumer;

public final class EventHandlerManager {
  // _(:3 」∠)_
  private static final Map<Plugin, EventHandlerManager> lazyMap = new HashMap<>();

  private final Map<Class<? extends Event>, IndexedListenerMap> manager;
  private final Plugin plugin;

  private final EventExecutor EXECUTOR = this::executor;

  private EventHandlerManager(Plugin plugin) {
    manager = new HashMap<>();
    this.plugin = plugin;
  }

  public static EventHandlerManager getInstance(Plugin plugin) {
    return lazyMap.computeIfAbsent(plugin, EventHandlerManager::new);
  }

  private void executor(Listener listener, Event event) {
    ((AdvancedListener) listener).call(event);
  }

  public <E extends Event> UUID addEventHandler(Class<E> eventClass, EventPriority priority, Consumer<E> eventHandler) {
    if (!EventClassValidate.test(eventClass))
      throw new RuntimeException("Class " + eventClass.getSimpleName() + " is not valid event class");
    manager.computeIfAbsent(eventClass, IndexedListenerMap::new);

    AdvancedListener listener = new AdvancedListener(eventClass, eventHandler);
    UUID uuid = UUID.randomUUID();
    plugin.getServer().getPluginManager()
        .registerEvent(eventClass, listener, priority, EXECUTOR, plugin);
    manager.get(eventClass).put(uuid, listener);
    return uuid;
  }

  public <E extends Event> UUID addEventHandler(Class<E> eventClass, Consumer<E> eventHandler) {
    return addEventHandler(eventClass, EventPriority.NORMAL, eventHandler);
  }

  public void unregister(UUID uuid) {
    manager.values().stream().parallel()
        .filter(map -> map.containsKey(uuid))
        .findFirst()
        .ifPresent(map -> map.remove(uuid));
  }

  public void unregister(String id) {
    unregister(UUID.fromString(id));
  }

  public void unregisterAll(Class<? extends Event> eventClass) {
    manager.computeIfPresent(eventClass, (ec, map) -> {
      map.clear();
      return null;
    });
  }

  public void unregisterAll() {
    manager.values().stream().parallel().forEach(IndexedListenerMap::clear);
    manager.clear();
  }

  public Collection<Class<? extends Event>> getRegisteredEvents() {
    return Collections.unmodifiableCollection(manager.keySet());
  }

  public Collection<UUID> getRegisteredListener(Class<? extends Event> eventClass) {
    return Optional.ofNullable(manager.get(eventClass))
        .map(HashMap::keySet)
        .map(Collections::unmodifiableCollection)
        .orElse(Collections.emptyList());
  }

  private static class AdvancedListener implements Listener {
    private final Consumer<Event> handler;

    private <E extends Event> AdvancedListener(Class<E> eventClass, Consumer<E> handler) {
      this.handler = event -> handler.accept(eventClass.cast(event));
    }

    // moshi moshi ~ event desu ka?
    private void call(Event event) {
      handler.accept(event);
    }

    private void unregister() {
      HandlerList.unregisterAll(this);
    }

  }

  private static class IndexedListenerMap extends HashMap<UUID, AdvancedListener> {

    private IndexedListenerMap(Class<? extends Event> eventClass) {
      super();
    }

    @Override
    public AdvancedListener remove(Object key) {
      AdvancedListener listener = super.remove(key);
      if (listener != null) listener.unregister();
      return listener;
    }

    @Override
    public void clear() {
      values().stream().parallel().forEach(AdvancedListener::unregister);
      super.clear();
    }

  }

}
