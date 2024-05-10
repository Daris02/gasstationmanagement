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

    @GetMapping( "/{stationId}")
    public List<StockMove> getAll(
            @PathVariable("stationId") Integer stationId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        return service.getAll(stationId, startDate, endDate);
    }

    @PostMapping({"", "/"})
    public StockMove create(@RequestBody StockMove stockMove) {
        return service.save(stockMove);
    }
}
