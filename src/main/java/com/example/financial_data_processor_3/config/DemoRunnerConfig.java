// src/main/java/com/example/financial_data_processor_3/config/DemoRunnerConfig.java
package com.example.financial_data_processor_3.config;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.financial_data_processor_3.platformsimulator.MockTcpServer;
import com.example.financial_data_processor_3.subscriber.Platform1Subscriber;
import com.example.financial_data_processor_3.subscriber.Platform2Subscriber;

@Configuration
public class DemoRunnerConfig {


    @Bean(initMethod = "start", destroyMethod = "stop")
    public MockTcpServer tcpServer() {
        MockTcpServer server = new MockTcpServer();
        try { server.start(5000); } catch (Exception e) { e.printStackTrace(); }
        return server;
    }


    @Bean
    CommandLineRunner demo(Coordinator coord,
                           Platform1Subscriber tcpSub,
                           Platform2Subscriber restSub) {
        return args -> {
            tcpSub.connect("Platform1", "u", "p");
            tcpSub.subscribe("Platform1", "USD/EUR");

            restSub.connect("Platform2", "u", "p");
            restSub.subscribe("Platform2", "BTC/USD");
        };
    }

}
