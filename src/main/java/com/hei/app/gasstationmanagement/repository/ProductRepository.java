package com.hei.app.gasstationmanagement.repository;

import com.hei.app.gasstationmanagement.config.ConnectionDB;
import com.hei.app.gasstationmanagement.model.AutoCRUD;
import com.hei.app.gasstationmanagement.model.Entity.Product;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

@Repository
public class ProductRepository extends AutoCRUD<Product, Integer> {
    @Override
    protected String getTableName() {
        return "product";
    }

    @Override
    protected Product mapResultSetToEntity(ResultSet resultSet) {
        try {
            return new Product(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Product> findAllByStationId(Integer id) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Set<Product> listAll = new HashSet<>();
        String query = "SELECT p.* FROM \"product\" p INNER JOIN \"stock\" s ON s.productId = p.id WHERE s.stationId = '" + id + "';";

        try {
            connection = ConnectionDB.createConnection();
            assert connection != null;
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                listAll.add(mapResultSetToEntity(resultSet));
            }
            return listAll;
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
