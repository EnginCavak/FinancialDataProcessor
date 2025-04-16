package com.example.financial_data_processor_3.model;

public class Rate {
    private String name;
    private double value;

    // constructors, getters, setters …

    @Override
    public String toString() {
        // Helpful formatted output
        return "Rate{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
