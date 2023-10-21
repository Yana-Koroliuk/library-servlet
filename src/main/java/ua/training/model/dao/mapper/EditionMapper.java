package ua.training.model.dao.mapper;

import ua.training.model.entity.Edition;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EditionMapper implements ObjectMapper<Edition> {

    @Override
    public Edition extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new Edition.Builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("edition_name_uk"))
                .anotherName(resultSet.getString("edition_name_en"))
                .build();
    }

    public Edition extractFromResultSetWithId(ResultSet resultSet, String id) throws SQLException {
        return new Edition.Builder()
                .id(resultSet.getInt(id))
                .name(resultSet.getString("edition_name_uk"))
                .anotherName(resultSet.getString("edition_name_en"))
                .build();
    }

}
