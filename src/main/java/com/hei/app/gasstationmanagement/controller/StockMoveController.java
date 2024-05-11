package com.hei.app.gasstationmanagement.controller;

import com.hei.app.gasstationmanagement.model.Entity.StockMove;
import com.hei.app.gasstationmanagement.service.StockMoveService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@AllArgsConstructor
@RequestMapping("/stockmoves")
public class StockMoveController {
    private final StockMoveService service;

    @GetMapping( "/{stationId}")
    public Object getAll(
            @PathVariable("stationId") Integer stationId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        return service.getAllWithDate(stationId, startDate, endDate);
    }

    @GetMapping( "/simple/{stationId}")
    public Object getAll(
            @PathVariable("stationId") Integer stationId
    ) {
        return service.getAllStockMoves(stationId);
    }

    @PostMapping({"", "/"})
    public StockMove create(@RequestBody StockMove stockMove) {
        return service.save(stockMove);
    }

    @GetMapping({"", "/"})
    public Object getAllMoving() {
        return service.getAll();
    }
    
}
