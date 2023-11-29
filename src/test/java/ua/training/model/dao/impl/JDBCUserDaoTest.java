package ua.training.model.dao.impl;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.User;
import ua.training.model.entity.enums.Role;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JDBCUserDaoTest {
    private JDBCUserDao userDao;
    private Connection mockedConnection;
    private PreparedStatement mockedStatement;
    private ResultSet mockedResultSet;

    @Before
    public void setUp() {
        mockedConnection = mock(Connection.class);
        mockedStatement = mock(PreparedStatement.class);
        mockedResultSet = mock(ResultSet.class);
        userDao = new JDBCUserDao(mockedConnection);
    }

    @Test
    public void create() throws SQLException {
        User user = new User.Builder()
                .login("login1")
                .password_hash("12e23cec2e")
                .isBlocked(false)
                .role(Role.READER)
                .build();

        when(mockedConnection.prepareStatement(SQLConstants.CREATE_USER, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(mockedStatement);
        when(mockedStatement.executeUpdate()).thenReturn(1);
        when(mockedStatement.getGeneratedKeys()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(true);
        when(mockedResultSet.getInt("id")).thenReturn(1);
        user.setId(1);
        User result = userDao.create(user).orElse(null);

        assertEquals(user, result);
    }

    @Test
    public void createFailure() throws SQLException {
        User user = new User.Builder()
                .login("login1")
                .password_hash("12e23cec2e")
                .isBlocked(false)
                .role(Role.READER)
                .build();

        when(mockedConnection.prepareStatement(SQLConstants.CREATE_USER, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(mockedStatement);
        when(mockedStatement.executeUpdate()).thenReturn(0);
        Optional<User> result = userDao.create(user);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void createExpectedException() throws SQLException {
        User user = new User.Builder()
                .login("login1")
                .password_hash("12e23cec2e")
                .isBlocked(false)
                .role(Role.READER)
                .build();

        when(mockedConnection.prepareStatement(SQLConstants.CREATE_USER, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(mockedStatement);
        when(mockedStatement.executeUpdate()).thenThrow(new SQLException());
        Optional<User> result = userDao.create(user);

        assertEquals(Optional.empty(), result);
    }

    @Test
    public void findByIdNotExistingUser() throws SQLException {
        when(mockedConnection.prepareStatement(SQLConstants.GET_USER_BY_ID)).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
        when(mockedResultSet.next()).thenReturn(false);

        Optional<User> result = userDao.findById(1);

        assertEquals(Optional.empty(), result);
    }

}