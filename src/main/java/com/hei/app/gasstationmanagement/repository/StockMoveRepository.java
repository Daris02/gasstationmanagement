package com.hei.app.gasstationmanagement.repository;

import com.hei.app.gasstationmanagement.config.ConnectionDB;
import com.hei.app.gasstationmanagement.model.AutoCRUD;
import com.hei.app.gasstationmanagement.model.Entity.StockMove;
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
public class StockMoveRepository extends AutoCRUD<StockMove, Integer> {
    private final ProductService productService;
    private final StationService stationService;

    @Override
    protected String getTableName() {
        return "stockmove";
    }

    @Override
    protected StockMove mapResultSetToEntity(ResultSet resultSet) {
        try {
            return new StockMove(
                    resultSet.getInt("id"),
                    resultSet.getString("type"),
                    resultSet.getDouble("amount"),
                    resultSet.getTimestamp("datetime").toInstant(),
                    resultSet.getBoolean("ismoney"),
                    stationService.getById(resultSet.getInt("stationId")),
                    productService.getById(resultSet.getInt("productId"))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StockMove save(StockMove toSave) {
        Connection connection = null;
        Statement statement = null;
        String insertQuery = "INSERT INTO stock (stationId, productId, amount, type, ismoney) VALUES (" +
            "( " + toSave.getStation().getId() + ", " + toSave.getProduct().getId() + ", " + toSave.getAmount() + "," + toSave.getType() + "," + toSave.getIsMoney() + ");";

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
