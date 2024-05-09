package com.hei.app.gasstationmanagement.model.Entity;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private Integer id;
    private Station station;
    private Product product;
    private Double quantity;
    private Instant datetime;
    private Double evaporationRate;
}
