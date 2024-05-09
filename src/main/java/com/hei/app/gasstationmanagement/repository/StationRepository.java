package com.hei.app.gasstationmanagement.repository;

import com.hei.app.gasstationmanagement.model.AutoCRUD;
import com.hei.app.gasstationmanagement.model.Entity.Station;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class StationRepository extends AutoCRUD<Station, Integer> {
    @Override
    protected String getTableName() {
        return "station";
    }

    @Override
    protected Station mapResultSetToEntity(ResultSet resultSet) {
        try {
            return new Station(
                    resultSet.getInt("id"),
                    resultSet.getString("location")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
