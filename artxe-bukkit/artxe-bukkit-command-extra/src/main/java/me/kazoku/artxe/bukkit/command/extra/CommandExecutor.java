package me.kazoku.artxe.bukkit.command.extra;

import org.bukkit.command.CommandSender;

public interface CommandExecutor {
  boolean execute(CommandSender sender, String label, String... args);
}
