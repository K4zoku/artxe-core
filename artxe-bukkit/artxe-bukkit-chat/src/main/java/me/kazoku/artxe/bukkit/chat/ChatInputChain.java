package me.kazoku.artxe.bukkit.chat;

import java.util.LinkedList;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ChatInputChain {
    private final UUID player;
    private final boolean cancel;
    private final ChatInput parent;

    ChatInputChain(UUID player, boolean cancel, ChatInput parent) {
        this.player = player;
        this.cancel = cancel;
        this.parent = parent;
    }

    public ChatInputChain then(Predicate<String> predicate) {
        parent.getHandlers().computeIfAbsent(player, uuid -> new LinkedList<>()).add(predicate);
        return new ChatInputChain(player, cancel, parent);
    }

    public ChatInputChain then(Consumer<String> consumer) {
        return then(msg -> {
            consumer.accept(msg);
            return true;
        });
    }
}
