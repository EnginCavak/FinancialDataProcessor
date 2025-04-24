package com.example.financial_data_processor_3.controller;

import com.example.financial_data_processor_3.coordinator.Coordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Platform 1 için REST endpoint sağlayıcısı.
 */
@RestController
@RequestMapping("/platform1")
public class Platform1Controller {

    private static final Logger log = LoggerFactory.getLogger(Platform1Controller.class);
    private final Coordinator coordinator;

    public Platform1Controller(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    @GetMapping("/subscribe")
    public String subscribe(@RequestParam String pair) {
        double randomPrice = ThreadLocalRandom.current().nextDouble(1000, 100000);
        coordinator.publishRawRate("platform1-raw", pair, randomPrice);
        log.info("Subscribed & pushed rate: {} = {}", pair, randomPrice);
        return "Subscribed & pushed rate: " + pair + " = " + randomPrice;
    }
}
