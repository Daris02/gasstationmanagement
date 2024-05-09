package com.hei.app.gasstationmanagement.controller;

import com.hei.app.gasstationmanagement.model.Entity.StockMove;
import com.hei.app.gasstationmanagement.service.StockMoveService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/stockmoves")
public class StockMoveController {
    private final StockMoveService service;

    @GetMapping({"", "/"})
    public List<StockMove> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public StockMove getById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @PostMapping({"", "/"})
    public StockMove create(@RequestBody StockMove stockMove) {
        return service.save(stockMove);
    }
}
