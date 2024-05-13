package com.hei.app.gasstationmanagement.service;

import com.hei.app.gasstationmanagement.model.Entity.Product;
import com.hei.app.gasstationmanagement.model.Entity.Station;
import com.hei.app.gasstationmanagement.repository.StationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StationService {
    private final StationRepository repository;
    private final ProductService productService;

    public List<Station> getAll() {
        List<Station> allStation = repository.findAll();
        List<Product> allProduct;
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
        station.setProducts(allProduct);
        return station;
    }

    public Station save(Station toSave) {
        repository.save(toSave);
        Optional<Station> station = repository.findAll().stream()
            .max(Comparator.comparingInt(Station::getId))
            .stream().findFirst();
        return station.orElse(toSave);
    }
}
