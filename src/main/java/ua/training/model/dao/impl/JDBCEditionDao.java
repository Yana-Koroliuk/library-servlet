package ua.training.model.dao.impl;

import ua.training.model.dao.EditionDao;
import ua.training.model.dao.mapper.EditionMapper;
import ua.training.model.entity.Edition;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class JDBCEditionDao implements EditionDao {
    private final Connection connection;

    public JDBCEditionDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Edition> create(Edition entity) {
        try (PreparedStatement statement =
                     connection.prepareStatement(SQLConstants.CREATE_EDITION, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getAnotherName());
            if (statement.executeUpdate() > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    entity.setId(resultSet.getInt("id"));
                    return Optional.of(entity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Edition> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Edition> findByNames(String name1, String name2) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.GET_EDITION_BY_NAME)) {
            statement.setString(1, name1);
            statement.setString(2, name2);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                EditionMapper mapper = new EditionMapper();
                Edition edition = mapper.extractFromResultSet(resultSet);
                return Optional.of(edition);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Edition> findAll() {
        return null;
    }

    @Override
    public void update(Edition entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
