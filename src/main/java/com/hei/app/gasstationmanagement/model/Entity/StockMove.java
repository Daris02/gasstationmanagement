package com.hei.app.gasstationmanagement.model.Entity;

import java.time.Instant;

import com.hei.app.gasstationmanagement.model.MoveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockMove {
    private Integer id;
    private MoveType type;
    private Double amount;
    private Instant datetime;
    private Boolean isMoney;
    private Station station;
    private Product product;
}
