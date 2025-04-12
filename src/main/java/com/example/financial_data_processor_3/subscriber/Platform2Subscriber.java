package com.example.financial_data_processor_3.subscriber;

public class Platform2Subscriber implements Subscriber {

    @Override
    public void connect(String platformName, String userId, String password) throws Exception {
        // REST endpoint üzerinden auth request...
        System.out.println("REST Subscriber connected to " + platformName + " with user " + userId);
    }

    @Override
    public void disconnect(String platformName, String userId, String password) throws Exception {
        // Belki session token iptali vb.
        System.out.println("REST Subscriber disconnected from " + platformName);
    }

    @Override
    public void subscribe(String platformName, String rateName) throws Exception {
        System.out.println("REST Subscriber subscribed to " + rateName);
        // Her x saniyede bir /mock/rate endpoint’ine istek atıp Rate verisi çekelim
        Thread pollThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String response = restTemplate.getForObject("http://localhost:8080/mock/rate", String.class);
                    // parse: "USD/EUR:1.23"
                    if (response != null) {
                        String[] parts = response.split(":");
                        if (parts.length == 2) {
                            Rate rate = new Rate(parts[0], Double.parseDouble(parts[1]));
                            // coordinator.onRateAvailable(platformName, rateName, rate)
                        }
                    }
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pollThread.start();
    }


    @Override
    public void unsubscribe(String platformName, String rateName) throws Exception {
        System.out.println("REST Subscriber unsubscribed from " + rateName + " on " + platformName);
    }
}
