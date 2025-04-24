package com.example.financial_data_processor_3.platformsimulator;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;

/**
 * A very small TCP server that keeps one client connected and
 * pushes a fake rate every second.
 *
 * Spring sees a public {@code start()} method (init-method) and calls it
 * right after the bean is created, so you do NOT start it manually elsewhere.
 */
public class MockTcpServer implements Runnable {

    private final int port;
    private Thread serverThread;

    /* ▼▼  REQUIRED: Spring’s init-method calls this                        ▼▼ */
    public void start() {
        serverThread = new Thread(this, "MockTCP-" + port);
        serverThread.setDaemon(true);
        serverThread.start();
    }
    /* ▲▲  ---------------------------------------------------------------- ▲▲ */

    /** Spring Bean constructor (no args → SubscriberLoader can also use it) */
    public MockTcpServer() {
        this(5000);                 // default port
    }

    /** Convenience constructor when you create it manually in code. */
    public MockTcpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("[MockTCP] listening on port " + port);
        try (ServerSocket server = new ServerSocket(port)) {
            Socket client = server.accept();                         // one client
            System.out.println("[MockTCP] client connected");
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream()));
            while (!server.isClosed()) {
                String line = "USD/EUR|" + (1.05 + Math.random() * 0.02)
                        + "|" + Instant.now();
                out.write(line);
                out.newLine();
                out.flush();
                Thread.sleep(1_000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
