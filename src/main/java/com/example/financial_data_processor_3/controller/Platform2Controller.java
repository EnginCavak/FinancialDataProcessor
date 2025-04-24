package com.example.financial_data_processor_3.controller;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import com.example.financial_data_processor_3.model.Rate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/platform2")
public class Platform2Controller {

    private final Coordinator coordinator;

    public Platform2Controller(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Basit demo: “EUR/USD” gibi bir pariteyi simüle eder,
     * Coordinator’a ham veriyi iletir.
     *
     * Örn.  GET /platform2/subscribe?pair=EUR/USD
     */
    @GetMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestParam String pair) {
        // Demo verisi üret – gerçekte Platform2Subscriber’dan gelir
        Rate rate = new Rate(pair, Math.random() * 100_000);
        coordinator.onRateAvailable("Platform2", pair, rate);
        return ResponseEntity.ok("Subscribed & pushed rate: " + rate);
    }
}
