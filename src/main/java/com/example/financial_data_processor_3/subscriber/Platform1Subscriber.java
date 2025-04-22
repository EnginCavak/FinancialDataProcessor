package com.example.financial_data_processor_3.subscriber;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import com.example.financial_data_processor_3.model.Rate;

@Component
public class Platform1Subscriber implements Subscriber {

    private Socket clientSocket;
    private Coordinator coordinator;   // injected via setter

    public Platform1Subscriber(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void connect(String platform, String user, String pwd) throws Exception {
        clientSocket = new Socket("localhost", 5000); // mock server port
        coordinator.onConnect(platform, true);
    }

    @Override
    public void subscribe(String platform, String rateName) {
        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()))) {

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        Rate rate = new Rate(parts[0], Double.parseDouble(parts[1]));
                        coordinator.onRateAvailable(platform, parts[0], rate);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override public void unsubscribe(String p, String r) { /* no‑op for mock */ }
    @Override public void disconnect(String p, String u, String pw) throws Exception {
        if (clientSocket != null) clientSocket.close();
        coordinator.onDisconnect(p, true);
    }
}
