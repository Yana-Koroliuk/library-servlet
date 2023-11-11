package ua.training.model.dao.impl;

import ua.training.model.dao.OrderDao;
import ua.training.model.dao.mapper.OrderMapper;
import ua.training.model.entity.Order;
import ua.training.model.entity.enums.OrderStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCOrderDao implements OrderDao {
    private final Connection connection;

    public JDBCOrderDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Order> create(Order entity) throws SQLException {
        try (PreparedStatement createOrder =
                     connection.prepareStatement(SQLConstants.CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement reserveBook = connection.prepareStatement(SQLConstants.UPDATE_AMOUNT_OF_BOOK)) {
            connection.setAutoCommit(false);
            createOrder.setLong(1, entity.getUser().getId());
            createOrder.setLong(2, entity.getBook().getId());
            createOrder.setObject(3, entity.getStartDate());
            createOrder.setObject(4, entity.getEndDate());
            createOrder.setString(5, entity.getOrderStatus().name());
            if (createOrder.executeUpdate() > 0) {
                ResultSet resultSet = createOrder.getGeneratedKeys();
                if (resultSet.next()) {
                    entity.setId(resultSet.getInt(1));
                    reserveBook.setInt(1, entity.getBook().getCount() - 1);
                    reserveBook.setLong(2, entity.getBook().getId());
                    reserveBook.executeUpdate();
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
            return Optional.of(entity);
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> findById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.GET_ORDER_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                OrderMapper mapper = new OrderMapper();
                Order order = mapper.extractFromResultSet(resultSet);
                return Optional.of(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        List<Order> orderList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQLConstants.GET_ALL_ORDERS);
            while (resultSet.next()) {
                OrderMapper mapper = new OrderMapper();
                Order order = mapper.extractFromResultSet(resultSet);
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public void update(Order entity) {
        try (PreparedStatement statement =
                     connection.prepareStatement(SQLConstants.UPDATE_ORDER)) {
            statement.setLong(1, entity.getUser().getId());
            statement.setLong(2, entity.getBook().getId());
            statement.setObject(3, entity.getStartDate());
            statement.setObject(4, entity.getEndDate());
            statement.setString(5, entity.getOrderStatus().name());
            statement.setLong(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long id) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.DELETE_ORDER_BY_ID)) {
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

    @Override
    public List<Order> findUserOrdersById(long userId) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        try (PreparedStatement getOrderStatement = connection.prepareStatement(SQLConstants.GET_ORDER_BY_USER_ID);
             PreparedStatement makeOrderOverdueStatement = connection.prepareStatement(SQLConstants.CHANGE_ORDER_STATUS_TO_OVERDUE);
             PreparedStatement deleteOrderStatement = connection.prepareStatement(SQLConstants.DELETE_ORDER_BY_ID)) {
            connection.setAutoCommit(false);
            getOrderStatement.setLong(1, userId);
            ResultSet resultSet = getOrderStatement.executeQuery();
            while (resultSet.next()) {
                OrderMapper orderMapper = new OrderMapper();
                Order order = orderMapper.extractFromResultSet(resultSet);
                if (LocalDate.now().isAfter(order.getEndDate())) {
                    if (order.getOrderStatus() == OrderStatus.APPROVED) {
                        makeOrderOverdueStatement.setLong(1, order.getId());
                        order.setOrderStatus(OrderStatus.OVERDUE);
                        makeOrderOverdueStatement.executeUpdate();
                    }
                    if (order.getOrderStatus() == OrderStatus.READER_HOLE) {
                        deleteOrderStatement.setLong(1, order.getId());
                        deleteOrderStatement.executeUpdate();
                    }
                }
                orderList.add(order);
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public List<Order> findOrdersWithOrderStatus(OrderStatus orderStatus) {
        List<Order> orderList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.GET_ORDERS_BY_STATUS)) {
            statement.setString(1, orderStatus.name());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OrderMapper orderMapper = new OrderMapper();
                Order order = orderMapper.extractFromResultSet(resultSet);
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public void cancelOrder(Order entity) throws SQLException {
        try (PreparedStatement cancelOrder = connection.prepareStatement(SQLConstants.UPDATE_ORDER);
             PreparedStatement returnBookReserve = connection.prepareStatement(SQLConstants.UPDATE_AMOUNT_OF_BOOK)) {
            connection.setAutoCommit(false);
            cancelOrder.setLong(1, entity.getUser().getId());
            cancelOrder.setLong(2, entity.getBook().getId());
            cancelOrder.setObject(3, entity.getStartDate());
            cancelOrder.setObject(4, entity.getEndDate());
            cancelOrder.setString(5, entity.getOrderStatus().name());
            cancelOrder.setLong(6, entity.getId());
            returnBookReserve.setInt(1, entity.getBook().getCount() + 1);
            returnBookReserve.setLong(2, entity.getBook().getId());
            cancelOrder.executeUpdate();
            returnBookReserve.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
    }
}
