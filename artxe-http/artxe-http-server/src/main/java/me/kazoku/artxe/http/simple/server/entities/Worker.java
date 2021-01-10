package me.kazoku.artxe.http.simple.server.entities;

import me.kazoku.artxe.http.simple.server.util.AsyncUtil;

import java.net.Socket;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Worker implements Runnable {

  protected final Socket client;
  protected final Set<Consumer<Socket>> handlers;
  protected final Logger logger;

  private Worker(Socket client, Set<Consumer<Socket>> handlers, Logger logger) {
    this.client = client;
    this.handlers = handlers;
    this.logger = logger;
  }

  public static void work(Socket client, Set<Consumer<Socket>> handlers, Logger logger) {
    AsyncUtil.runAsync(new Worker(client, handlers, logger));
  }

  public Logger getLogger() {
    return logger;
  }

  public void run() {
    for (Consumer<Socket> handler : handlers) handler.accept(client);
  }
}
