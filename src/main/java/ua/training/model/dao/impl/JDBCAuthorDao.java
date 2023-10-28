package ua.training.model.dao.impl;

import ua.training.model.dao.AuthorDao;
import ua.training.model.dao.mapper.AuthorMapper;
import ua.training.model.entity.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCAuthorDao implements AuthorDao {
    private final Connection connection;

    public JDBCAuthorDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Author> create(Author author) {
        try (PreparedStatement statement =
                     connection.prepareStatement(SQLConstants.CREATE_AUTHOR, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(2, author.getAnotherName());
            statement.setString(1, author.getName());
            if (statement.executeUpdate() > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    author.setId(resultSet.getInt("id"));
                    return Optional.of(author);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Author> findById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.GET_AUTHOR_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                AuthorMapper mapper = new AuthorMapper();
                Author author = mapper.extractFromResultSet(resultSet);
                return Optional.of(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Author> findByNames(String name1, String name2) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.GET_AUTHOR_BY_NAME)) {
            statement.setString(1, name1);
            statement.setString(2, name2);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                AuthorMapper mapper = new AuthorMapper();
                Author author = mapper.extractFromResultSet(resultSet);
                return Optional.of(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Author> findAll() {
        return null;
    }

    @Override
    public void update(Author entity) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<Author> getAuthorsByBookId(long id) {
        List<Author> authors = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQLConstants.GET_AUTHORS_BY_BOOK_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    AuthorMapper mapper = new AuthorMapper();
                    Author author = mapper.extractFromResultSetWithId(resultSet, "author_id");
                    authors.add(author);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
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
