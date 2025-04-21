package com.example.financial_data_processor_3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor          // required by JPA / deserialisers
@AllArgsConstructor         // (String name, double value)
public class Rate {

    private String name;
    private double value;
}
