package ua.training.model.dao.impl;

import ua.training.model.dao.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCDaoFactory extends DaoFactory {
    private final DataSource dataSource = ConnectionPoolHolder.getDataSource();

    @Override
    public UserDao createUserDao() {
        return new JDBCUserDao(getConnection());
    }

    @Override
    public EditionDao createEditionDao() {
        return new JDBCEditionDao(getConnection());
    }

    @Override
    public AuthorDao createAuthorDao() {
        return new JDBCAuthorDao(getConnection());
    }

    @Override
    public BookDao createBookDao() {
        return new JDBCBookDao(getConnection());
    }

    @Override
    public OrderDao createOrderDao() {
        return new JDBCOrderDao(getConnection());
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
