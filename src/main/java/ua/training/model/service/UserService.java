package ua.training.model.service;

import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.UserDao;
import ua.training.model.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public boolean singUpUser(User user) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.create(user);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<User> findById(long id) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> findByLogin(String login) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findByLogin(login);
        }
    }

    public List<User> findAll() {
        try (UserDao userDao = daoFactory.createUserDao()) {
            return userDao.findAll();
        }
    }

    public boolean delete(long id) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            userDao.delete(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean blockUser(User user) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            user.setBlocked(true);
            userDao.update(user);
            return true;
        }
    }

    public boolean unBlockUser(User user) {
        try (UserDao userDao = daoFactory.createUserDao()) {
            user.setBlocked(false);
            userDao.update(user);
            return true;
        }
    }
}
