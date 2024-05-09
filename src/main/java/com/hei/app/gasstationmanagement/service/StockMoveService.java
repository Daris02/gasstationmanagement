package com.hei.app.gasstationmanagement.service;

import com.hei.app.gasstationmanagement.model.Entity.StockMove;
import com.hei.app.gasstationmanagement.repository.StockMoveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StockMoveService {
    private final StockMoveRepository repository;

    public List<StockMove> getAll() {
        return repository.findAll();
    }

    public StockMove getById(Integer id) {
        return repository.getById(id);
    }

    public StockMove save(StockMove toSave) {
        return repository.save(toSave);
    }
}
