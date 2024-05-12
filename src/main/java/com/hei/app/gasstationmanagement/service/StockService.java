package com.hei.app.gasstationmanagement.service;

import com.hei.app.gasstationmanagement.model.Entity.Stock;
import com.hei.app.gasstationmanagement.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class StockService {
    private final StockRepository repository;

    public List<Stock> getAll() {
        return repository.findAll();
    }

    public Stock getById(Integer id) {
        return repository.getById(id);
    }

    public Stock save(Stock toSave) {
        toSave.setDatetime(Instant.now());
        return repository.save(toSave);
    }

    public List<Stock> findAllByStationId(Integer stationId) {
        return repository.findAll(stationId);
    }

    public Stock getByStationAndProduct(Integer stationId, Integer productId) {
        return repository.findByStationAndProduct(stationId, productId);
    }

    public Stock getLastUpdate(Integer stationId, Integer productId, Instant instant) {
        return repository.findLastUpdateEntry(stationId, productId, instant);
    }
}
