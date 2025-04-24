package com.example.financial_data_processor_3.service;

import com.example.financial_data_processor_3.model.RateFields;
import org.springframework.stereotype.Service;

@Service
public class RateCalculator {
    public RateFields calculate(double rawPrice) {
        RateFields f = new RateFields();

        f.setOpen(rawPrice);
        f.setClose(rawPrice);

        double spread = rawPrice * 0.001;
        double bid = rawPrice - spread;
        double ask = rawPrice + spread;

        f.setHigh(Math.max(rawPrice, ask));
        f.setLow(Math.min(rawPrice, bid));

        f.setBid(bid);
        f.setAsk(ask);
        f.setMid((bid + ask) / 2.0);

        return f;
    }
}
