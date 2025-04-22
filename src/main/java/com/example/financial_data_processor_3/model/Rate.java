package com.example.financial_data_processor_3.model;

public class Rate {

    private String name;
    private double value;

    // no‑arg constructor for frameworks
    public Rate() {}

    // handy all‑args constructor
    public Rate(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Rate{" + "name='" + name + '\'' + ", value=" + value + '}';
    }
}
