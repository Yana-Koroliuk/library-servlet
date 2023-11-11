package ua.training.model.dao.mapper;

import ua.training.model.entity.Book;
import ua.training.model.entity.Order;
import ua.training.model.entity.User;
import ua.training.model.entity.enums.OrderStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderMapper implements ObjectMapper<Order> {

    @Override
    public Order extractFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User.Builder()
                .id(resultSet.getInt(2))
                .build();
        Book book = new Book.Builder()
                .id(resultSet.getInt(3))
                .build();
        return new Order.Builder()
                .id(resultSet.getInt(1))
                .user(user)
                .book(book)
                .startDate(LocalDate.parse(resultSet.getString(4)))
                .endDate(LocalDate.parse(resultSet.getString(5)))
                .orderStatus(OrderStatus.valueOf(resultSet.getString(6)))
                .build();
    }
}
