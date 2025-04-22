package com.example.financial_data_processor_3.config;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.platformsimulator.MockTcpServer;
import com.example.financial_data_processor_3.subscriber.Platform1Subscriber;
import com.example.financial_data_processor_3.subscriber.Platform2Subscriber;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoRunnerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public MockTcpServer tcpServer() {
        return new MockTcpServer();      // starts on port 5000 via start()
    }

    @Bean
    CommandLineRunner demo(Coordinator coordinator,
                           Platform1Subscriber tcpSub,
                           Platform2Subscriber restSub) {
        return args -> {
            tcpSub.connect("Platform1", "u", "p");
            tcpSub.subscribe("Platform1", "USD/EUR");

            restSub.connect("Platform2", "u", "p");
            restSub.subscribe("Platform2", "BTC/USD");

            // quick manual call just to prove things work
            coordinator.onRateAvailable("Demo", "TEST/RATE",
                    new Rate("TEST/RATE", 9.99));
        };
    }
}
