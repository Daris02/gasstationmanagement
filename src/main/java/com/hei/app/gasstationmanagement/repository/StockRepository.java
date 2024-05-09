package com.hei.app.gasstationmanagement.repository;

import com.hei.app.gasstationmanagement.config.ConnectionDB;
import com.hei.app.gasstationmanagement.model.AutoCRUD;
import com.hei.app.gasstationmanagement.model.Entity.Stock;
import com.hei.app.gasstationmanagement.service.ProductService;
import com.hei.app.gasstationmanagement.service.StationService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
@AllArgsConstructor
public class StockRepository extends AutoCRUD<Stock, Integer> {
    private final ProductService productService;
    private final StationService stationService;

    @Override
    protected String getTableName() {
        return "stock";
    }

    @Override
    protected Stock mapResultSetToEntity(ResultSet resultSet) {
        try {
            return new Stock(
                    resultSet.getInt("id"),
                    stationService.getById(resultSet.getInt("stationId")),
                    productService.getById(resultSet.getInt("productId")),
                    resultSet.getDouble("quantity"),
                    resultSet.getTimestamp("datetime").toInstant(),
                    resultSet.getDouble("evaporationRate")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stock save(Stock toSave) {
        Connection connection = null;
        Statement statement = null;
        String insertQuery = "INSERT INTO stock (stationId, productId, quantity, evaporationRate) VALUES (" +
            "( " + toSave.getStation().getId() + ", " + toSave.getProduct().getId() + ", " + toSave.getQuantity() + "," + toSave.getEvaporationRate() + ");";

        try {
            connection = ConnectionDB.createConnection();
            assert connection != null;
            statement = connection.createStatement();
            statement.executeUpdate(insertQuery);
            return toSave;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
