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
import java.util.ArrayList;
import java.util.List;

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
        String insertQuery = "INSERT INTO stockmove (stationId, productId, amount, type, datetime, ismoney) VALUES " +
            "( " + toSave.getStation().getId() + ", " + toSave.getProduct().getId() + ", " + toSave.getAmount() + ", '" + toSave.getType() + "', '" + toSave.getDatetime() + "', "+ toSave.getIsMoney() + ");";

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

    public List<StockMove> findAll(Integer stationId) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<StockMove> stockMoves = new ArrayList<>();

        try {
            connection = ConnectionDB.createConnection();
            statement = connection.createStatement();

            String selectQuery = "SELECT * FROM \"stockmove\"" +
                    "WHERE stationid = " + stationId + ";";
            resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                stockMoves.add(mapResultSetToEntity(resultSet));
            }
            return stockMoves;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public StockMove getLastEntryByStationAndProduct(Integer stationId, Integer productId) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StockMove stockMove = null;

        try {
            connection = ConnectionDB.createConnection();
            statement = connection.createStatement();

            String selectQuery = "SELECT * FROM \"stockmove\"" +
                    "WHERE stationid = " + stationId + " " +
                    "AND productid = " + productId + " " +
                    "AND type = 'entry' " +
                    "ORDER BY datetime DESC " +
                    "LIMIT 1 ;";
            resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                stockMove = mapResultSetToEntity(resultSet);
            }
            return stockMove;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
