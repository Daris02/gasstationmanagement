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

    @GetMapping({"", "/"})
    public Object getAll() {
        return service.getAll();
    }

    @GetMapping( "/{stockeMoveId}")
    public Object getById(@PathVariable("stockeMoveId") Integer stockeMoveId) {
        return service.getById(stockeMoveId);
    }

    @PostMapping({"", "/"})
    public StockMove create(@RequestBody StockMove stockMove) {
        return service.save(stockMove);
    }
    
    @GetMapping( "/tableview/{stationId}")
    public Object getAll(
            @PathVariable("stationId") Integer stationId
    ) {
        return service.getAllStockMoves(stationId);
    }
    
    @GetMapping( "/station/{stationId}")
    public Object getAllByStationIdWithDetails(
            @PathVariable("stationId") Integer stationId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        return service.getAllByStationId(stationId, startDate, endDate);
    }

    @GetMapping("/globalview")
    public Object getAllGlobalView() {
        return service.getAllGlobalView();
    }
    
}
