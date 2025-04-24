package com.example.financial_data_processor_3.config;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.integration.RateKafkaProducer;
import com.example.financial_data_processor_3.platformsimulator.MockTcpServer;
import com.example.financial_data_processor_3.repository.RateCacheRepository;
import com.example.financial_data_processor_3.subscriber.Platform1Subscriber;
import com.example.financial_data_processor_3.subscriber.Platform2Subscriber;
import com.example.financial_data_processor_3.subscriber.Subscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Beans that help the demo run (mock TCP server, mock subscribers, coordinator).
 */
@Configuration
public class DemoRunnerConfig {

    /* ----------------------------------------------------------------
       1)  TCP mock server
       ---------------------------------------------------------------- */
    @Bean(initMethod = "start")             // Spring will call server.start()
    public MockTcpServer tcpServer() {
        return new MockTcpServer(5000);     // no manual start(5000) here!
    }

    /* ----------------------------------------------------------------
       2)  Coordinator — wired with cache + Kafka producer
       ---------------------------------------------------------------- */
    @Bean
    public Coordinator coordinator(RateCacheRepository cache,
                                   RateKafkaProducer producer) {
        return new Coordinator(cache, producer);
    }

    /* ----------------------------------------------------------------
       3)  Demo subscribers that talk to the coordinator
       ---------------------------------------------------------------- */
    @Bean
    public List<Subscriber> demoSubscribers(Coordinator coordinator) {
        Platform1Subscriber tcpSub  = new Platform1Subscriber(coordinator);
        Platform2Subscriber restSub = new Platform2Subscriber(coordinator);
        return List.of(tcpSub, restSub);
    }

    /* ----------------------------------------------------------------
       4)  (Optional) run a quick demo after startup
       ---------------------------------------------------------------- */
    /*
    @Bean
    public ApplicationRunner demoRunner(List<Subscriber> subs) {
        return args -> {
            subs.forEach(s -> s.connect("demo", "user", "pass"));
            subs.forEach(s -> s.subscribe("demo", "USD/EUR"));
            Thread.sleep(5_000);            // keep app alive long enough to see data
            subs.forEach(s -> s.disconnect("demo", "user", "pass"));
        };
    }
    */
}
