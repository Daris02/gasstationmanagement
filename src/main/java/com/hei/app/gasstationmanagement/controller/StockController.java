package com.hei.app.gasstationmanagement.controller;

import com.hei.app.gasstationmanagement.model.Entity.Stock;
import com.hei.app.gasstationmanagement.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/stocks")
public class StockController {
    private final StockService service;

    @GetMapping({"", "/"})
    public List<Stock> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Stock getById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @PostMapping({"", "/"})
    public Stock create(@RequestBody Stock stock) {
        return service.save(stock);
    }
}
