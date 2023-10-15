package ua.training.model.dao;

import ua.training.model.entity.Order;
import ua.training.model.entity.enums.OrderStatus;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao extends GenericDao<Order> {
    List<Order> findUserOrdersById(long id) throws SQLException;
    List<Order> findOrdersWithOrderStatus(OrderStatus orderStatus);
    void cancelOrder(Order order) throws SQLException;
}

