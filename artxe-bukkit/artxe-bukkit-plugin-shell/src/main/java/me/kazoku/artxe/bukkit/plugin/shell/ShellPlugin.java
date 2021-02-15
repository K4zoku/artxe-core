package me.kazoku.artxe.bukkit.plugin.shell;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class ShellPlugin extends JavaPlugin {

  protected ShellPlugin() {
    super();
    initialize();
  }

  protected ShellPlugin(final JavaPluginLoader loader, final PluginDescriptionFile description, final File dataFolder, final File file) {
    super(loader, description, dataFolder, file);
    initialize();
  }

  public void initialize() {
    // EMPTY
  }

  public void load() {
    // EMPTY
  }

  public void startup() {
    // EMPTY
  }

  public void postStartup() {
    // EMPTY
  }

  public void shutdown() {
    // EMPTY
  }

  public final void terminate() {
    HandlerList.unregisterAll(this);
    getServer().getScheduler().cancelTasks(this);
  }

  @Override
  public final void onLoad() {
    load();
  }

  @Override
  public final void onEnable() {
    startup();
    getServer().getScheduler().runTask(this, this::postStartup);
  }

  @Override
  public final void onDisable() {
    shutdown();
    terminate();
  }
}
