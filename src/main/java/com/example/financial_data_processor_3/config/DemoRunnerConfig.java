// src/main/java/com/example/financial_data_processor_3/config/DemoRunnerConfig.java
package com.example.financial_data_processor_3.config;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoRunnerConfig {

    @Bean
    CommandLineRunner demo(Coordinator coordinator) { // Spring injects Coordinator
        return args -> {
            Rate sample = new Rate("USD/EUR", 1.09);   // constructor now valid
            coordinator.onRateAvailable("DemoPlatform", "USD/EUR", sample);
        };
    }
}
