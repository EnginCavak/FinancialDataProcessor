package com.example.financial_data_processor_3.platformsimulator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class MockRestController {

    @GetMapping("/mock/rate")
    public String randomRate() {
        String[] symbols = {"BTC/USD", "ETH/USD", "XAU/USD"};
        Random rnd = new Random();
        return symbols[rnd.nextInt(symbols.length)] + ":" +
                (10 + rnd.nextDouble() * 1000);
    }
}
