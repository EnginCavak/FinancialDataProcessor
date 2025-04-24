package com.example.financial_data_processor_3.model;
import lombok.Data;

@Data
public class RateFields {
    private double open;
    private double close;
    private double high;
    private double low;
    private double bid;
    private double ask;
    private double mid;
    // constructor, getters, setters, toString
}
