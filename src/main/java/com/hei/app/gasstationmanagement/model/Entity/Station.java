package com.hei.app.gasstationmanagement.model.Entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Station {
    private Integer id;
    private String location;
    private List<Product> products = new ArrayList<>();

    public Station(Integer id, String location) {
        this.id = id;
        this.location = location;
    }
}
