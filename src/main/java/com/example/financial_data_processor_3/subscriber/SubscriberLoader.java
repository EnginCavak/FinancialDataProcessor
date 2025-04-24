package com.example.financial_data_processor_3.subscriber;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Dynamically loads Subscriber implementations listed in
 * src/main/resources/subscribers.properties.
 *
 * File format:
 *   subscriber.1=com.example.….Platform1Subscriber
 *   subscriber.2=com.example.….Platform2Subscriber
 */
public final class SubscriberLoader {

    private static final String CONFIG_FILE = "subscribers.properties";
    private static final String PREFIX = "subscriber.";

    private SubscriberLoader() {
        /* utility class – no instances */
    }

    public static List<Subscriber> loadSubscribers() throws IOException {
        // 1) Load the properties file from the class-path
        Properties props = new Properties();
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(
                        "'" + CONFIG_FILE + "' not found on classpath. " +
                                "Place it in src/main/resources.");
            }
            props.load(in);
        }

        // 2) Instantiate each class whose key starts with "subscriber."
        List<Subscriber> subscribers = new ArrayList<>();

        for (String key : props.stringPropertyNames()) {
            if (key.startsWith(PREFIX)) {
                String className = props.getProperty(key).trim();
                try {
                    Class<?> clazz = Class.forName(className);
                    subscribers.add((Subscriber) clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    throw new IllegalStateException(
                            "Failed to load Subscriber: " + className, e);
                }
            }
        }
        return subscribers;
    }
}
