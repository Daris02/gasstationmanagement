package com.hei.app.gasstationmanagement.service;

import com.hei.app.gasstationmanagement.model.Entity.Product;
import com.hei.app.gasstationmanagement.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Integer id) {
        Product product = repository.getById(id);
        if (product == null) return null;
        return product;
    }

    public Product save(Product toSave) {
        return repository.save(toSave);
    }

    public List<Product> getAllByStationId(Integer id) {
        return repository.findAllByStationId(id);
    }
}
