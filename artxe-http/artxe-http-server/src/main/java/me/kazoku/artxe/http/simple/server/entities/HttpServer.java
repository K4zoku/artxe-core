package me.kazoku.artxe.http.simple.server.entities;

import me.kazoku.artxe.http.simple.server.util.AsyncUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer implements Runnable {
  protected final Logger logger;
  protected int serverPort;
  protected ServerSocket serverSocket;
  protected boolean isStopped = false;
  protected Set<Consumer<Socket>> handlers;

  private HttpServer(int port, Logger logger) {
    this.serverPort = port;
    this.handlers = new LinkedHashSet<>();
    this.logger = logger;
  }

  public static HttpServer runServer(int port, Logger logger) {
    HttpServer server = new HttpServer(port, logger);
    AsyncUtil.runAsync(server);
    return server;
  }

  public static HttpServer runServer(int port) {
    return runServer(port, Logger.getAnonymousLogger());
  }

  public void run() {
    start();
  }

  public void start() {
    openServerSocket();
    while (!isStopped()) {
      Socket client;
      try {
        client = this.serverSocket.accept();
      } catch (IOException e) {
        if (isStopped()) getLogger().warning("Server stopped, could not accept client request...");
        else getLogger().log(Level.WARNING, "Error while accepting client connection", e);
        return;
      }
      Worker.work(client, getHandlers(), getLogger());
    }
    getLogger().info("Server stopped.");
  }

  public Logger getLogger() {
    return logger;
  }

  public void addHandler(Consumer<Socket> handler) {
    this.handlers.add(handler);
  }

  public Set<Consumer<Socket>> getHandlers() {
    return handlers;
  }

  private synchronized boolean isStopped() {
    return this.isStopped;
  }

  public synchronized void stop() {
    try {
      this.serverSocket.close();
      this.isStopped = true;
    } catch (IOException e) {
      throw new RuntimeException("Error while closing server", e);
    }
  }

  public synchronized void restart() {
    if (!isStopped()) stop();
    openServerSocket();
  }

  private void openServerSocket() {
    try {
      this.serverSocket = new ServerSocket(this.serverPort);
      this.isStopped = false;
    } catch (IOException e) {
      throw new RuntimeException("Cannot open port " + serverPort, e);
    }
  }

}
