package com.hei.app.gasstationmanagement.service;

import com.hei.app.gasstationmanagement.model.Entity.Stock;
import com.hei.app.gasstationmanagement.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        repository.save(toSave);
        Optional<Stock> stock = repository.findAll().stream()
                .max(Comparator.comparingInt(Stock::getId))
                .stream().findFirst();
        return stock.orElse(toSave);
    }

    public List<Stock> findAllByStationId(Integer stationId) {
        return repository.findAll(stationId);
    }

    public Stock getByStationAndProduct(Integer stationId, Integer productId) {
        return repository.findByStationAndProduct(stationId, productId);
    }

    public Stock getLastUpdate(Integer stationId, Integer productId, Instant instant) {
        Stock stock = repository.findLastUpdateEntry(stationId, productId, instant.minusSeconds(14400));
        return stock;
    }
}
