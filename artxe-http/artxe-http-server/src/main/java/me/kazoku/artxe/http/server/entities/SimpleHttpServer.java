package me.kazoku.artxe.http.server.entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class SimpleHttpServer implements Runnable {
    protected int serverPort;
    protected ServerSocket serverSocket;
    protected boolean isStopped = false;
    protected final boolean async;
    protected BiConsumer<InputStream, OutputStream> action;

    private SimpleHttpServer(int port, BiConsumer<InputStream, OutputStream> action, boolean async) {
        this.serverPort = port;
        this.action = action;
        this.async = async;
    }

    public static SimpleHttpServer runServer(int port, BiConsumer<InputStream, OutputStream> action, boolean async) {
        SimpleHttpServer server = new SimpleHttpServer(port, action, async);
        if (async) CompletableFuture.runAsync(server).join();
        else server.run();
        return server;
    }

    public void run() {
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException("Error while accepting client connection", e);
            }
            Worker.work(clientSocket, getAction(), async);
        }
        System.out.println("Server Stopped.");
    }

    public void setAction(BiConsumer<InputStream, OutputStream> action) {
        this.action = action;
        if (!isStopped()) {
            stop();
        }
        openServerSocket();
    }

    public BiConsumer<InputStream, OutputStream> getAction() {
        return action;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while closing server", e);
        }
    }

    private void openServerSocket() {
        this.isStopped = false;
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

}
