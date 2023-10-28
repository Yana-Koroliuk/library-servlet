package ua.training.model.dao.impl;

public abstract class SQLConstants {

    public static final String CREATE_USER = "INSERT INTO users (login, password_hash, user_role, is_blocked)" +
            " VALUES (?, ?, ?::\"role\", ?)";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    public static final String GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";
    public static final String GET_ALL_USERS = "SELECT * FROM users";
    public static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    public static final String UPDATE_USER = "UPDATE users SET (login, password_hash, is_blocked) = (?, ?, ?) WHERE  id = ? ;";

    public static final String CREATE_AUTHOR = "INSERT INTO author (full_name_uk, full_name_en) VALUES (?, ?)";
    public static final String GET_AUTHOR_BY_ID = "SELECT * FROM author WHERE id = ?";
    public static final String GET_AUTHOR_BY_NAME = "SELECT * FROM author WHERE full_name_uk = ? AND full_name_en = ?";
    public static final String GET_AUTHORS_BY_BOOK_ID = "SELECT * FROM authorship INNER JOIN author ON " +
            "author_id = author.id WHERE book_id = ?;";
}
