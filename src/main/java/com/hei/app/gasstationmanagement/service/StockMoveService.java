package com.hei.app.gasstationmanagement.service;

import com.hei.app.gasstationmanagement.config.DefaultValue;
import com.hei.app.gasstationmanagement.model.Entity.Stock;
import com.hei.app.gasstationmanagement.model.Entity.StockMove;
import com.hei.app.gasstationmanagement.repository.StockMoveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StockMoveService {
    private final StockMoveRepository repository;
    private final StockService stockService;

    public List<StockMove> getAll(Integer stationId, String startDate, String endDate) {
        List<StockMove> allStockMove = repository.findAll(stationId);
        List<StockMove> filteredStockMoves = new ArrayList<>();
        if (startDate == null || endDate == null) return  allStockMove;

        Instant startInstant = Timestamp.valueOf(startDate).toInstant().minusSeconds(14400);
        Instant endInstant = Timestamp.valueOf(endDate).toInstant().minusSeconds(14400);
        for (StockMove stockMove : allStockMove) {
            Instant stockMoveDate = stockMove.getDatetime().minusSeconds(3600);
            if ((stockMoveDate.isAfter(startInstant)) && stockMoveDate.isBefore(endInstant)) {
                filteredStockMoves.add(stockMove);
            }
        }
        return filteredStockMoves;
    }

    public StockMove getById(Integer id) {
        return repository.getById(id);
    }

    public StockMove save(StockMove toSave) {
        switch (toSave.getType()) {
            // -- -- SUPPLY
            case "entry":
                Stock newStock = stockService.getByStationId(toSave.getStation().getId(), toSave.getProduct().getId());
                newStock.setQuantity(newStock.getQuantity() + toSave.getAmount());
                stockService.save(newStock);

            // -- -- SALE
            case "out":
                Stock lastStock = stockService.getByStationId(toSave.getStation().getId(), toSave.getProduct().getId());
                System.out.println(lastStock);
                if (lastStock.getQuantity() > toSave.getAmount() && toSave.getAmount() < DefaultValue.QUANTITY_MAX) {
                    lastStock.setQuantity(lastStock.getQuantity() - toSave.getAmount());
                }
              stockService.save(lastStock);
        }

        toSave.setDatetime(Instant.now());
        return repository.save(toSave);
    }
}
