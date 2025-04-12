package com.example.financial_data_processor_3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class MockRestController {

    @GetMapping("/mock/rate")
    public String getRandomRate() {
        String[] currencies = {"USD/EUR", "GBP/USD", "JPY/EUR"};
        Random rnd = new Random();
        String currency = currencies[rnd.nextInt(currencies.length)];
        double value = 0.5 + (rnd.nextDouble() * 2);
        return currency + ":" + value;
    }
}
