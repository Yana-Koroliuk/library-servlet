package ua.training.model.service;

import ua.training.model.dao.BookDao;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.OrderDao;
import ua.training.model.dao.UserDao;
import ua.training.model.entity.Book;
import ua.training.model.entity.Order;
import ua.training.model.entity.User;
import ua.training.model.entity.enums.OrderStatus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService {
    DaoFactory daoFactory = DaoFactory.getInstance();

    public boolean orderBook(Order order) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            orderDao.create(order);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Order> findAllWithStatus(OrderStatus orderStatus) {
        try (OrderDao orderDao = daoFactory.createOrderDao();
            UserDao userDao = daoFactory.createUserDao();
            BookDao bookDao = daoFactory.createBookDao()) {
            List<Order> orderList = orderDao.findOrdersWithOrderStatus(orderStatus);
            return getOrders(userDao, bookDao, orderList);
        }
    }

    private List<Order> getOrders(UserDao userDao, BookDao bookDao, List<Order> orderList) {
        List<Order> list = new ArrayList<>();
        try {
            for (Order order : orderList) {
                User user = userDao.findById(order.getUser().getId()).orElse(new User.Builder().build());
                Book book = bookDao.findByIdWithLocaled(order.getBook().getId()).orElse(new Book.Builder().build());
                order.setUser(user);
                order.setBook(book);
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> findByUserId(long userId) {
        try (UserDao userDao = daoFactory.createUserDao();
            OrderDao orderDao = daoFactory.createOrderDao();
            BookDao bookDao = daoFactory.createBookDao()) {
            List<Order> orderList = orderDao.findUserOrdersById(userId);
            return getOrders(userDao, bookDao, orderList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean approveOrder(long orderId) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            Optional<Order> optionalOrder = orderDao.findById(orderId);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setOrderStatus(OrderStatus.APPROVED);
                orderDao.update(order);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean cancelOrder(long id) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            Optional<Order> optionalOrder = orderDao.findById(id);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setOrderStatus(OrderStatus.CANCELED);
                orderDao.cancelOrder(order);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOrder(long id) {
        try (OrderDao orderDao = daoFactory.createOrderDao()) {
            orderDao.delete(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
