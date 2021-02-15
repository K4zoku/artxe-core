package me.kazoku.artxe.bukkit.chat.v2;

import me.kazoku.artxe.bukkit.event.EventHandlerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChatInputAPI {
  private static final Map<UUID, ChatInputAPI> HANDLERS_MAP = new HashMap<>();

  private final UUID playerId;
  private boolean hidden = true; // cancel chat event
  private boolean completed = false;
  private boolean stopOnFailure = true;
  private Predicate<String> criteria = f -> true;
  private Consumer<String> onTrue = empty();
  private Consumer<String> onFalse = empty();
  private ChatInputAPI next = null;

  private ChatInputAPI(UUID playerId) {
    this.playerId = playerId;
  }

  public static void register(final Plugin plugin) {
    register(plugin, true);
  }

  public static void register(final Plugin plugin, final boolean clearOnQuit) {
    EventHandlerManager manager = EventHandlerManager.getInstance(plugin);
    manager.addEventHandler(AsyncPlayerChatEvent.class, ChatInputAPI::onChat);
    if (clearOnQuit) manager.addEventHandler(PlayerQuitEvent.class, ChatInputAPI::onQuit);
  }

  public static ChatInputAPI newChain(final Player player) {
    return newChain(player.getUniqueId());
  }

  public static <T> Consumer<T> empty() {
    return t -> {
    };
  }

  public static ChatInputAPI newChain(final UUID playerId) {
    final ChatInputAPI chain = new ChatInputAPI(playerId);
    HANDLERS_MAP.put(playerId, chain);
    return chain;
  }

  private static void onChat(AsyncPlayerChatEvent event) {
    final UUID playerId = event.getPlayer().getUniqueId();

    ChatInputAPI chain = HANDLERS_MAP.get(playerId);
    while (chain.isCompleted() && chain.hasNext()) {
      chain = chain.next;
    }

    if (chain.isCompleted()) {
      HANDLERS_MAP.remove(playerId);
      return;
    }

    if (chain.hidden) event.setCancelled(true);
    chain.onMessage(event.getMessage());
  }

  private static void onQuit(PlayerQuitEvent event) {
    HANDLERS_MAP.remove(event.getPlayer().getUniqueId());
  }

  public ChatInputAPI criteria(Predicate<String> criteria) {
    this.criteria = criteria;
    return this;
  }

  public ChatInputAPI onTrue(Consumer<String> onTrue) {
    this.onTrue = onTrue;
    return this;
  }

  public ChatInputAPI onFalse(Consumer<String> onFalse) {
    this.onFalse = onFalse;
    return this;
  }

  public ChatInputAPI hideChat() {
    this.hidden = true;
    return this;
  }

  public ChatInputAPI showChat() {
    this.hidden = false;
    return this;
  }

  public boolean isCompleted() {
    return this.completed;
  }

  public ChatInputAPI restartOnFalse() {
    this.stopOnFailure = false;
    return this;
  }

  public ChatInputAPI stopOnFalse() {
    this.stopOnFailure = true;
    return this;
  }

  public boolean hasNext() {
    return this.next != null;
  }

  public ChatInputAPI next() {
    return hasNext() ? this.next : (this.next = new ChatInputAPI(playerId));
  }

  private void onMessage(String message) {
    if (criteria.test(message)) {
      onTrue.accept(message);
      completed = true;
    } else {
      onFalse.accept(message);
      completed = stopOnFailure;
    }
  }

}
