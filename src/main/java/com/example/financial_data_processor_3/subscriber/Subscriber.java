package com.example.financial_data_processor_3.subscriber;

public interface Subscriber {
    void connect(String platformName, String userId, String password) throws Exception;
    void disconnect(String platformName, String userId, String password) throws Exception;
    void subscribe(String platformName, String rateName) throws Exception;
    void unsubscribe(String platformName, String rateName) throws Exception;
}
