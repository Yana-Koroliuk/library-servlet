package ua.training.model.dao.impl;

import ua.training.model.dao.UserDao;
import ua.training.model.dao.mapper.UserMapper;
import ua.training.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCUserDao implements UserDao {
    private final Connection connection;

    public JDBCUserDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<User> create(User entity) {
        try (PreparedStatement statement =
                     connection.prepareStatement(SQLConstants.CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getPasswordHash());
            statement.setString(3, entity.getRole().name());
            statement.setBoolean(4, entity.isBlocked());
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
    public Optional<User> findById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.GET_USER_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UserMapper mapper = new UserMapper();
                User user = mapper.extractFromResultSet(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.GET_USER_BY_LOGIN)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UserMapper mapper = new UserMapper();
                User user = mapper.extractFromResultSet(resultSet);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQLConstants.GET_ALL_USERS);
            while (resultSet.next()) {
                UserMapper mapper = new UserMapper();
                User user = mapper.extractFromResultSet(resultSet);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public void update(User entity) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.UPDATE_USER)) {
            statement.setString(1, entity.getLogin());
            statement.setString(2, entity.getPasswordHash());
            statement.setBoolean(3, entity.isBlocked());
            statement.setLong(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.DELETE_USER_BY_ID)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
