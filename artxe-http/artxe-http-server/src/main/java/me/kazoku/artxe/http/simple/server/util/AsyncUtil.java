package me.kazoku.artxe.http.simple.server.util;

import java.util.concurrent.CompletableFuture;

public class AsyncUtil {
  public static void runAsync(Runnable runnable) {
    CompletableFuture.runAsync(runnable).join();
  }
}
