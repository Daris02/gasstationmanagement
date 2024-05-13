package com.hei.app.gasstationmanagement.service;

import com.hei.app.gasstationmanagement.Exception.QuantityExcessExcpetion;
import com.hei.app.gasstationmanagement.model.Entity.Product;
import com.hei.app.gasstationmanagement.model.Entity.Station;
import com.hei.app.gasstationmanagement.model.Entity.Stock;
import com.hei.app.gasstationmanagement.model.Entity.StockMove;
import com.hei.app.gasstationmanagement.repository.StockMoveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class StockMoveService {
    private final StockMoveRepository repository;
    private final StockService stockService;
    private final ProductService productService;
    private final StationService stationService;
    private final static double QUANTITY_MAX = 200;

    public List<StockMove> getAll() {
        return repository.findAll();
    }

    public StockMove getById(Integer id) {
        return repository.getById(id);
    }

    public StockMove save(StockMove toSave) {
        Stock lastStock = stockService.getByStationAndProduct(toSave.getStation().getId(), toSave.getProduct().getId());
        Double productPrice = productService.getById(toSave.getProduct().getId()).getPrice();

        switch (toSave.getType()) {
            // -- -- SUPPLY
            case "entry":
                lastStock.setQuantity(lastStock.getQuantity() + toSave.getAmount());
                stockService.save(lastStock);
                break;
            // -- -- SALE
            case "out":
                if (toSave.getIsMoney()) {
                    double amount = toSave.getAmount() /productPrice;
                    toSave.setAmount((Math.round(amount * 100) / 100.0));
                }
                if (lastStock.getQuantity() > toSave.getAmount() && toSave.getAmount() < QUANTITY_MAX) {
                    lastStock.setQuantity(lastStock.getQuantity() - toSave.getAmount());
                } else {
                    throw new QuantityExcessExcpetion("Amount is not allowed to exceed quantity limit (<200L)");
                }
                stockService.save(lastStock);
                break;
        }
        toSave.setDatetime(Instant.now());
        return repository.save(toSave);
    }

    public List<Map<String, Object>> getAllGlobalView() {
        List<Station> allStations = stationService.getAll();
        List<StockMove> allStockMoves = repository.findAll();
        List<Map<String, Object>> respones = new ArrayList<>();

        for (Station station : allStations) {
            Map<String, Object> result = new LinkedHashMap<>();

            for (StockMove move : allStockMoves) {
                List<Map<String, Object>> allList = getAllByStationId(station.getId(), null, null);
                Double essenceQte = 0.0;
                Double gasoilQte = 0.0;
                Double petrolQte = 0.0;
                Double essenceRestant = 0.0;
                Double gasoilRestant = 0.0;
                Double petrolRestant = 0.0;
                Product product = move.getProduct();

                result.put("ID Station", move.getStation().getId());                
                for (Map<String , Object> obj : allList) {
                    switch (product.getId()) {
                        case 1:
                            essenceQte += amountConverter(move,(Double) obj.get("Qte Vendue Essence"), product.getPrice());
                            break;
                    
                        case 2:
                            gasoilQte += amountConverter(move, (Double) obj.get("Qte Vendue Gasoil"), product.getPrice());
                            break;
                    
                        case 3:
                            petrolQte += amountConverter(move, (Double) obj.get("Qte Vendue Pretrol"), product.getPrice());
                            break;
                    }
                    if (obj==allList.get(allList.size()-1)) {
                        essenceRestant = (Double) obj.get("Qte Restante Essence");
                        gasoilRestant = (Double) obj.get("Qte Restante Gasoil");
                        petrolRestant = (Double) obj.get("Qte Restante Pretrol");
                    }
                }
                result.put("Qte Vendue Essence", essenceQte);
                result.put("Montant essence", productService.getById(1).getPrice() * essenceQte);
                result.put("Qte Vendue Gasoil", gasoilQte);
                result.put("Montant gasoil", productService.getById(2).getPrice() * gasoilQte);
                result.put("Qte Vendue Pretrol", petrolQte);
                result.put("Montant petrol", productService.getById(3).getPrice() * petrolQte);
                result.put("Montant total de la station", 
                    (productService.getById(1).getPrice() * essenceQte) + 
                    (productService.getById(2).getPrice() * gasoilQte) + 
                    (productService.getById(3).getPrice() * petrolQte));
                result.put("Qte Restante Essence", essenceRestant);
                result.put("Qte Restante Gasoil", gasoilRestant);
                result.put("Qte Restante Pretrol", petrolRestant);
            }
            respones.add(result);
        }
        return respones;
    }

    public List<Map<String, Object>> getAllByStationId(Integer stationId, String startDate, String endDate) {
        List<StockMove> allStockMove = repository.findAll(stationId);
        List<StockMove> filteredStockMoves = new ArrayList<>();
        List<Stock> allStocks = getAllStocks(stationId);
        if (startDate == null || endDate == null) return getAllStockMovesWithDate(allStockMove, allStocks);
        Instant startInstant = Timestamp.valueOf(startDate).toInstant().minusSeconds(14400);
        Instant endInstant = Timestamp.valueOf(endDate).toInstant().minusSeconds(14400);
        for (StockMove stockMove : allStockMove) {
            Instant stockMoveDate = stockMove.getDatetime().minusSeconds(3600);
            if ((stockMoveDate.isAfter(startInstant)) && stockMoveDate.isBefore(endInstant)) {
                filteredStockMoves.add(stockMove);
            }
        }
        return getAllStockMovesWithDate(filteredStockMoves, allStocks);
    }

    public List<Map<String, Object>> getAllStockMoves(Integer stationId) {
        List<StockMove> allStockMove = repository.findAll(stationId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (StockMove stockMove : allStockMove) {
            Map<String, Object> result = new HashMap<>();
            result.put("Date", dateFormatter(stockMove.getDatetime()));
            result.put("Product", stockMove.getProduct().getName());
            result.put("MoveType", stockMove.getType());
            result.put("Quantity", stockMove.getAmount());
            response.add(result);
        }
        return response;
    }

    private List<Stock> getAllStocks(Integer stationId) {
        return stockService.findAllByStationId(stationId);
    }

    private List<Map<String, Object>> getAllStockMovesWithDate(List<StockMove> stockMoves, List<Stock> allStocks) {
        List<Map<String, Object>> response = new ArrayList<>();
        for (StockMove stockMove : stockMoves) {
            Map<String, Object> result = new HashMap<>();
            Optional<Stock> matchingStock = allStocks.stream()
                    .filter(s -> isEqualsDate(s.getDatetime(), stockMove.getDatetime()))
                    .findFirst();

            if (matchingStock.isPresent()) {
                Stock stock = matchingStock.get();
                result.put("id", stockMove.getId());
                result.put("Date", dateFormatter(stockMove.getDatetime()));
                result.put("Qte Ajout Essence", 0.0);
                result.put("Qte Ajout Gasoil", 0.0);
                result.put("Qte Ajout Pretrol", 0.0);

                result.put("Qte Vendue Essence", 0.0);
                result.put("Qte Vendue Gasoil", 0.0);
                result.put("Qte Vendue Pretrol", 0.0);

                result.put("Qte Restante Essence", 0.0);
                result.put("Qte Restante Gasoil", 0.0);
                result.put("Qte Restante Pretrol", 0.0);
                response.add(updateMapResult(stock, stockMove, result));
            }
        }
        return response;
    }

    private Map<String, Object> updateMapResult(Stock stock, StockMove stockMove, Map<String, Object> map) {
        StockMove lastStockMoveEntry = repository.getLastEntryByStationAndProduct(stockMove.getStation().getId(), stockMove.getProduct().getId());
        Stock stockUpdate = addEvaporationRate(stock, stockService.getLastUpdate(stock.getStation().getId(), stock.getProduct().getId(), lastStockMoveEntry.getDatetime()));
        switch (stockMove.getType()) {
            case "entry":
                switch (stockMove.getProduct().getId()) {
                    case 1:
                        map.replace("Qte Ajout Essence", stockMove.getAmount());
                        map.replace("Qte Restante Essence", stockUpdate.getQuantity());
                        break;
                    case 2:
                        map.replace("Qte Ajout Gasoil", stockMove.getAmount());
                        map.replace("Qte Restante Gasoil", stockUpdate.getQuantity());
                        break;
                    case 3:
                        map.replace("Qte Ajout Pretrol", stockMove.getAmount());
                        map.replace("Qte Restante Pretrol", stockUpdate.getQuantity());
                        break;
                }
                break;

            case "out":
                switch (stockMove.getProduct().getId()) {
                    case 1:
                        map.replace("Qte Vendue Essence", stockMove.getAmount());
                        map.replace("Qte Restante Essence", stockUpdate.getQuantity() - stockMove.getAmount());
                        break;
                    case 2:
                        map.replace("Qte Vendue Gasoil", stockMove.getAmount());
                        map.replace("Qte Restante Gasoil", stockUpdate.getQuantity() - stockMove.getAmount());
                        break;
                    case 3:
                        map.replace("Qte Vendue Pretrol", stockMove.getAmount());
                        map.replace("Qte Restante Pretrol", stockUpdate.getQuantity() - stockMove.getAmount());
                        break;
                }
                break;
        }
        return map;
    }

    private double amountConverter(StockMove stockMove, double amount, double productPrice) {
        if (stockMove.getIsMoney()) {
            amount = stockMove.getAmount() / productPrice;
        }
        return (Math.round(amount * 100) / 100.0);
    }

    private String dateFormatter(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        return dateTime.format(formatter);
    }

    private boolean isEqualsDate(Instant first, Instant second) {
        ZoneId zoneId = ZoneId.of("UTC");
        LocalDateTime firstTruncated = LocalDateTime.ofInstant(first, zoneId).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime secondTruncated = LocalDateTime.ofInstant(second, zoneId).truncatedTo(ChronoUnit.MINUTES);
        return firstTruncated.equals(secondTruncated);
    }

    private Stock addEvaporationRate(Stock stockNow, Stock lastStock) {
        long storageDuration = ChronoUnit.DAYS.between(lastStock.getDatetime(), stockNow.getDatetime().plusSeconds(10800));
        if (storageDuration >= 1) stockNow.setQuantity(stockNow.getQuantity() - (stockNow.getEvaporationRate() * storageDuration));
        return stockNow;
    }

}
