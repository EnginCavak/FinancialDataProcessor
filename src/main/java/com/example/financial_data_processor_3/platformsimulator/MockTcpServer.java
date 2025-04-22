package com.example.financial_data_processor_3.platformsimulator;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class MockTcpServer {

    private ServerSocket serverSocket;
    private volatile boolean running = false;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        System.out.println("[MockTCP] started on port " + port);

        new Thread(() -> {
            while (running) {
                try (Socket client = serverSocket.accept();
                     OutputStream out = client.getOutputStream()) {

                    String[] symbols = {"USD/EUR", "GBP/USD", "JPY/EUR"};
                    Random rnd = new Random();
                    String line = symbols[rnd.nextInt(symbols.length)] + ":" +
                            (0.5 + rnd.nextDouble() * 2) + "\n";

                    out.write(line.getBytes());
                    out.flush();

                } catch (IOException e) {
                    if (running) e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() throws IOException {
        running = false;
        if (serverSocket != null) serverSocket.close();
        System.out.println("[MockTCP] stopped.");
    }
}
