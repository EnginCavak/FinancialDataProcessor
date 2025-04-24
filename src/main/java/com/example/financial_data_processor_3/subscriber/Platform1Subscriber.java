package com.example.financial_data_processor_3.subscriber;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Component
public class Platform1Subscriber implements Subscriber {

    private Socket clientSocket;
    private Coordinator coordinator;       // will be set later via setter

    /* ----------------------------------------
       ① no-arg constructor for reflection
    -----------------------------------------*/
    public Platform1Subscriber() {
        // left empty on purpose
    }

    /* ----------------------------------------
       ② optional constructor-injection path
          (not used by SubscriberLoader)
    -----------------------------------------*/
    public Platform1Subscriber(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    /* Setter so we can still inject the coordinator
       after reflective construction                      */
    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    /* ------------------ interface impl. ------------------ */

    @Override
    public void connect(String platform, String user, String pwd) throws Exception {
        clientSocket = new Socket("localhost", 5000);   // mock TCP server
        coordinator.onConnect(platform, true);
    }

    @Override
    public void subscribe(String platform, String rateName) {
        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()))) {

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(":");     // e.g.  USD/EUR:1.0825
                    if (parts.length == 2) {
                        Rate rate = new Rate(parts[0], Double.parseDouble(parts[1]));
                        coordinator.onRateAvailable(platform, parts[0], rate);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override public void unsubscribe(String p, String r) {/* no-op for demo */}

    @Override
    public void disconnect(String p, String u, String pw) throws Exception {
        if (clientSocket != null) clientSocket.close();
        coordinator.onDisconnect(p, true);
    }
}
