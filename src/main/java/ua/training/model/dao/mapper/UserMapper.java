package ua.training.model.dao.mapper;

import ua.training.model.entity.User;
import ua.training.model.entity.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ObjectMapper<User> {

    @Override
    public User extractFromResultSet(ResultSet resultSet) throws SQLException {
        return new User.Builder()
                .id(resultSet.getLong("id"))
                .login(resultSet.getString("login"))
                .password_hash(resultSet.getString("password_hash"))
                .role( Role.valueOf(resultSet.getString("user_role")))
                .isBlocked(resultSet.getBoolean("is_blocked"))
                .build();
    }
}
