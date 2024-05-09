package com.hei.app.gasstationmanagement.controller;

import com.hei.app.gasstationmanagement.model.Entity.Product;
import com.hei.app.gasstationmanagement.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    @GetMapping({"", "/"})
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @PostMapping({"", "/"})
    public Product create(@RequestBody Product product) {
        return service.save(product);
    }
}
