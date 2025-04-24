package com.example.financial_data_processor_3.subscriber;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Platform2Subscriber implements Subscriber {

    private final RestTemplate restTemplate = new RestTemplate();
    private Coordinator coordinator;

    /* ① no-arg constructor (needed by SubscriberLoader) */
    public Platform2Subscriber() {}

    /* ② optional constructor injection */
    public Platform2Subscriber(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    /* ------------------ interface impl. ------------------ */

    @Override
    public void connect(String platform, String user, String pwd) {
        coordinator.onConnect(platform, true);
    }

    @Override
    public void subscribe(String platform, String rateName) {
        // very simple polling demo (every 2 s)
        new Thread(() -> {
            while (true) {
                try {
                    ResponseEntity<String> res =
                            restTemplate.getForEntity("http://localhost:8081/rate/" + rateName,
                                    String.class);
                    if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null) {
                        double value = Double.parseDouble(res.getBody());
                        Rate rate = new Rate(rateName, value);
                        coordinator.onRateAvailable(platform, rateName, rate);
                    }
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    @Override public void unsubscribe(String p, String r) {/* no-op */}

    @Override
    public void disconnect(String p, String u, String pw) {
        coordinator.onDisconnect(p, true);
    }
}
