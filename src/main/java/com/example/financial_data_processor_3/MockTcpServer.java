package com.example.financial_data_processor_3;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class MockTcpServer {

    private ServerSocket serverSocket;
    private boolean running = false;

    public void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        System.out.println("Mock TCP Server started on port " + port);

        new Thread(() -> {
            while (running) {
                try {
                    Socket client = serverSocket.accept();
                    System.out.println("Client connected: " + client.getInetAddress());
                    OutputStream out = client.getOutputStream();
                    sendRandomData(out);
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendRandomData(OutputStream out) throws IOException {
        // Rastgele dummy bir "rate" verisi üretiyoruz, örnek: "USD/EUR:1.07"
        String[] currencies = {"USD/EUR", "GBP/USD", "JPY/EUR"};
        Random rnd = new Random();
        String currency = currencies[rnd.nextInt(currencies.length)];
        double value = 0.5 + (rnd.nextDouble() * 2); // 0.5 ile 2.5 arası
        String line = currency + ":" + value + "\n";

        out.write(line.getBytes());
        out.flush();
    }

    public void stopServer() throws IOException {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        System.out.println("Mock TCP Server stopped.");
    }
}
