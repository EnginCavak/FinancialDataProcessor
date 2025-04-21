import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.web.client.RestTemplate;
import com.example.financial_data_processor_3.model.Rate;


package com.example.financial_data_processor_3.subscriber;

public class Platform1Subscriber implements Subscriber {

    @Override
    public void connect(String platformName, String userId, String password) throws Exception {
        Socket socket = new Socket("localhost", 5000);
        // istersen userId/password kontrolü simule edebilirsin
        this.clientSocket = socket;
        System.out.println("TCP connected to " + platformName);
    }


    @Override
    public void disconnect(String platformName, String userId, String password) throws Exception {
        // Soketi kapatma...
        System.out.println("TCP Subscriber disconnected from " + platformName);
    }

    @Override
    public void subscribe(String platformName, String rateName) throws Exception {
        // Yeni bir thread ile socket’in input stream’ini dinleyelim
        Thread listenerThread = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // line örnek: "USD/EUR:1.07"
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String currency = parts[0];
                        double value = Double.parseDouble(parts[1]);
                        // Koordinatöre ilet (Rate oluştur)
                        Rate rate = new Rate(currency, value);
                        // coordinator.onRateAvailable(platformName, currency, rate) gibi
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.start();
        System.out.println("TCP Subscriber subscribed to " + rateName);
    }


    @Override
    public void unsubscribe(String platformName, String rateName) throws Exception {
        // abonelik iptal...
        System.out.println("TCP Subscriber unsubscribed from " + rateName);
    }
}
