package com.hei.app.gasstationmanagement.controller;

import com.hei.app.gasstationmanagement.model.Entity.Station;
import com.hei.app.gasstationmanagement.service.StationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/stations")
public class StationController {
    private final StationService service;

    @GetMapping({"", "/"})
    public List<Station> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Station getById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @PostMapping({"", "/"})
    public Station create(@RequestBody Station station) {
        return service.save(station);
    }
}
