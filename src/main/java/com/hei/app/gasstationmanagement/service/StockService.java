package com.hei.app.gasstationmanagement.service;

import com.hei.app.gasstationmanagement.model.Entity.Stock;
import com.hei.app.gasstationmanagement.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        return repository.save(toSave);
    }
}
