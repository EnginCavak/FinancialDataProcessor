package com.example.financial_data_processor_3.service;

import com.example.financial_data_processor_3.model.RateFields;
import org.springframework.stereotype.Service;

/** Raw fiyatı alır, spread hesaplar, tüm alanları döndürür */
@Service
public class RateCalculator {

    public RateFields calculate(double raw) {

        double spread = raw * 0.001;      // %0,1
        double bid  = raw - spread;
        double ask  = raw + spread;
        double mid  = (bid + ask) / 2.0;

        return new RateFields(
                raw,          // open
                raw,          // close
                Math.max(raw, ask),   // high
                Math.min(raw, bid),   // low
                bid, ask, mid
        );
    }
}
