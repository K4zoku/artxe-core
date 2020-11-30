package me.kazoku.artxe.bukkit.chat;

import me.kazoku.artxe.bukkit.event.EventHandlerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Predicate;

public final class ChatInput {

    /**
     * Map
     *   UUID: Player UUID
     *   Queue:
     *     Entry:
     *       Runnable: Call before run
     *       Predicate: Call while run, return value used for {@link AsyncPlayerChatEvent#setCancelled)}
     */
    private final Map<UUID, Queue<Predicate<String>>> handlers;
    // todo: unregister
    private final EventHandlerManager manager;
    private final List<UUID> registeredListener;

    public ChatInput(Plugin plugin) {
        this(plugin, true);
    }

    public ChatInput(Plugin plugin, boolean clearQueueOnQuit) {
        handlers = new HashMap<>();
        registeredListener = new ArrayList<>();
        manager = EventHandlerManager.getInstance(plugin);
        registeredListener.add(manager.addEventHandler(AsyncPlayerChatEvent.class, this::onChat));
        if (clearQueueOnQuit) registeredListener.add(manager.addEventHandler(PlayerQuitEvent.class, this::onQuit));

    }

    public ChatInputChain getPlayerInput(UUID player, boolean cancel) {
        return new ChatInputChain(player, cancel, this);
    }

    public ChatInputChain getPlayerInput(Player player) {
        return getPlayerInput(player.getUniqueId(), true);
    }

    public void clearInputQueue(UUID player) {
        Optional.ofNullable(handlers.get(player)).ifPresent(Queue::clear);
    }

    private void onChat(AsyncPlayerChatEvent event) {
        Optional.ofNullable(getHandlers().get(event.getPlayer().getUniqueId()))
                .filter(queue -> !queue.isEmpty())
                .ifPresent(queue -> event.setCancelled(queue.poll().test(event.getMessage())));
    }

    private void onQuit(PlayerQuitEvent event) {
        clearInputQueue(event.getPlayer().getUniqueId());
    }

    Map<UUID, Queue<Predicate<String>>> getHandlers() {
        return handlers;
    }

    public void unregister() {
        registeredListener.forEach(manager::unregister);
    }

}