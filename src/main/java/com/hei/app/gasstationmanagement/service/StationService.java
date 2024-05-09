package com.hei.app.gasstationmanagement.service;

import com.hei.app.gasstationmanagement.model.Entity.Product;
import com.hei.app.gasstationmanagement.model.Entity.Station;
import com.hei.app.gasstationmanagement.repository.StationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StationService {
    private final StationRepository repository;
    private final ProductService productService;

    public List<Station> getAll() {
        List<Station> allStation = repository.findAll();
        List<Product> allProduct = new ArrayList<>();
        for (Station station : allStation) {
            allProduct = productService.getAllByStationId(station.getId());
            station.setProducts(allProduct);
        }
        return allStation;
    }

    public Station getById(Integer id) {
        Station station = repository.getById(id);
        if (station == null) return null;
        List<Product> allProduct = productService.getAllByStationId(station.getId());
        if (allProduct.isEmpty()) station.setProducts(List.of());
        station.setProducts(allProduct);
        return station;
    }

    public Station save(Station toSave) {
        return repository.save(toSave);
    }
}
