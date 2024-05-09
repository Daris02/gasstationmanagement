package com.hei.app.gasstationmanagement.model.Entity;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockMove {
    private Integer id;
    private String type;
    private Double amount;
    private Instant datetime;
    private Boolean isMoney;
    private Station station;
    private Product product;
}
