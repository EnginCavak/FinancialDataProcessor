package com.example.financial_data_processor_3.subscriber;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Platform2Subscriber implements Subscriber {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Coordinator coordinator;

    public Platform2Subscriber(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void connect(String platform, String user, String pwd) {
        coordinator.onConnect(platform, true); // mock auth always succeeds
    }

    @Override
    public void subscribe(String platform, String rateName) {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String resp = restTemplate.getForObject(
                            "http://localhost:8080/mock/rate", String.class);
                    if (resp != null && resp.contains(":")) {
                        String[] p = resp.split(":");
                        Rate rate = new Rate(p[0], Double.parseDouble(p[1]));
                        coordinator.onRateAvailable(platform, p[0], rate);
                    }
                    Thread.sleep(2000);
                } catch (Exception e) { e.printStackTrace(); }
            }
        }).start();
    }

    @Override public void unsubscribe(String p, String r) {}
    @Override public void disconnect(String p, String u, String pw) {}
}
