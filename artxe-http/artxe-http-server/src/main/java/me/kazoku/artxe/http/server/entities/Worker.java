package me.kazoku.artxe.http.server.entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class Worker implements Runnable {

    protected Socket client;
    protected BiConsumer<InputStream, OutputStream> action;

    private Worker(Socket client, BiConsumer<InputStream, OutputStream> action) {
        this.client = client;
        this.action = action;
    }

    public static void work(Socket client, BiConsumer<InputStream, OutputStream> action, boolean async) {
        Worker worker = new Worker(client, action);
        if (async) CompletableFuture.runAsync(worker);
        else worker.run();
    }

    public void run() {
        try {
            action.accept(client.getInputStream(), client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
