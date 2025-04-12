package com.example.financial_data_processor_3.subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SubscriberLoader {

    public static List<Subscriber> loadSubscribers(String configFile) throws IOException {
        List<Subscriber> subscribers = new ArrayList<>();
        Properties props = new Properties();

        try (InputStream in = SubscriberLoader.class.getResourceAsStream(configFile)) {
            props.load(in);
        }

        String classNames = props.getProperty("subscriber.classes");
        for (String className : classNames.split(",")) {
            try {
                Class<?> clazz = Class.forName(className.trim());
                Subscriber sub = (Subscriber) clazz.getDeclaredConstructor().newInstance();
                subscribers.add(sub);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subscribers;
    }
}
