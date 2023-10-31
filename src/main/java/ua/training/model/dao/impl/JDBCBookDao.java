package ua.training.model.dao.impl;

import ua.training.controller.filters.LocalizationFilter;
import ua.training.model.dao.BookDao;
import ua.training.model.dao.mapper.AuthorMapper;
import ua.training.model.dao.mapper.BookMapper;
import ua.training.model.entity.Author;
import ua.training.model.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCBookDao implements BookDao {
    private final Connection connection;

    public JDBCBookDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Book> create(Book entity) {
        try (PreparedStatement statement =
                     connection.prepareStatement(SQLConstants.CREATE_BOOK, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setLong(3, entity.getEdition().getId());
            statement.setString(4, entity.getLanguage());
            statement.setObject(5, entity.getPublicationDate());
            statement.setBigDecimal(6, entity.getPrice());
            statement.setInt(7, entity.getCount());
            statement.setString(8, entity.getAnotherTitle());
            statement.setString(9, entity.getAnotherDescription());
            statement.setString(10, entity.getAnotherLanguage());
            if (statement.executeUpdate() > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    entity.setId(resultSet.getInt(1));
                    return Optional.of(entity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void delete(long id) throws SQLException {
        try (PreparedStatement deleteAuthorship = connection.prepareStatement(SQLConstants.UNSET_AUTHORSHIP_BY_BOOK_ID);
             PreparedStatement deleteBook = connection.prepareStatement(SQLConstants.DELETE_BOOK)) {
            connection.setAutoCommit(false);
            deleteAuthorship.setLong(1, id);
            deleteAuthorship.executeUpdate();
            deleteBook.setLong(1, id);
            deleteBook.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
    }


    @Override
    public Optional<Book> findById(long id) throws SQLException {
        try (PreparedStatement getBookStatement = connection.prepareStatement(SQLConstants.GET_PARTIAL_BOOK_BY_ID);
             PreparedStatement getAuthorsStatement = connection.prepareStatement(SQLConstants.GET_AUTHORS_BY_BOOK_ID)) {
            connection.setAutoCommit(false);
            getBookStatement.setLong(1, id);
            Book book = null;
            ResultSet bookResultSet = getBookStatement.executeQuery();
            if (bookResultSet.next()) {
                BookMapper bookMapper = new BookMapper();
                book = bookMapper.extractFromResultSet(bookResultSet);
                getAuthorsStatement.setLong(1, book.getId());
                List<Author> authors = new ArrayList<>();
                ResultSet authorsResultSet = getAuthorsStatement.executeQuery();
                while (authorsResultSet.next()) {
                    AuthorMapper authorMapper = new AuthorMapper();
                    Author author = authorMapper.extractFromResultSet(authorsResultSet);
                    authors.add(author);
                }
                book.setAuthors(authors);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return book == null ? Optional.empty() : Optional.of(book);
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> findByIdWithLocaled(long id) {
        try (PreparedStatement getBookStatement = connection.prepareStatement(SQLConstants.GET_PARTIAL_BOOK_BY_ID);
             PreparedStatement getAuthorsStatement = connection.prepareStatement(SQLConstants.GET_AUTHORS_BY_BOOK_ID)) {
            connection.setAutoCommit(false);
            getBookStatement.setLong(1, id);
            ResultSet bookResultSet = getBookStatement.executeQuery();
            Book book = null;
            if (bookResultSet.next()) {
                book = getBookWithAuthors(getAuthorsStatement, bookResultSet);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return book == null ? Optional.empty() : Optional.of(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> findByTitleAndAuthorsNames(String title, List<Author> authors) {
        StringBuilder stringNamesArray = new StringBuilder();
        stringNamesArray.append("{");
        for (Author author: authors) {
            stringNamesArray.append("\"")
                    .append(author.getName())
                    .append("\"")
                    .append(",");
        }
        stringNamesArray.deleteCharAt(stringNamesArray.length()-1);
        stringNamesArray.append("}");
        try (PreparedStatement statement =
                     connection.prepareStatement(SQLConstants.GET_PARTIAL_BOOK_BY_TITLE_AND_AUTHORS)) {
            statement.setString(1, title);
            statement.setString(2, stringNamesArray.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                BookMapper mapper = new BookMapper();
                Book book = mapper.extractFromResultSet(resultSet);
                return Optional.of(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findByKeyWord(String keyWord, String sortBy, String sortType, int page) {
        List<Book> bookList = new ArrayList<>();
        String query = chooseSortingQuery(sortBy, sortType);
        try (PreparedStatement statement =
                     connection.prepareStatement(query);
             PreparedStatement findAuthors = connection.prepareStatement(SQLConstants.GET_AUTHORS_BY_BOOK_ID)) {
            statement.setString(1, keyWord);
            statement.setString(2, keyWord);
            int offsetPosition = (page-1)*4;
            statement.setInt(3, 4);
            statement.setInt(4, offsetPosition);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = getBookWithAuthors(findAuthors, resultSet);
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    @Override
    public long getBookAmountWithKeyWord(String keyWord) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.GET_AMOUNT_OF_BOOKS_WITH_KEY_WORD)) {
            statement.setString(1, keyWord);
            statement.setString(2, keyWord);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();
        try (Statement getBookStatement = connection.createStatement();
             PreparedStatement getAuthorsStatement = connection.prepareStatement(SQLConstants.GET_AUTHORS_BY_BOOK_ID)) {
            ResultSet resultSet = getBookStatement.executeQuery(SQLConstants.GET_ALL_BOOKS);
            connection.setAutoCommit(false);
            while (resultSet.next()) {
                Book book = getBookWithAuthors(getAuthorsStatement, resultSet);
                bookList.add(book);
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    @Override
    public void update(Book entity) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.UPDATE_PARTIAL_BOOK)) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setString(3, entity.getLanguage());
            statement.setLong(4, entity.getEdition().getId());
            statement.setBigDecimal(6, entity.getPrice());
            statement.setObject(5, entity.getPublicationDate());
            statement.setInt(7, entity.getCount());
            statement.setString(8, entity.getAnotherTitle());
            statement.setString(9, entity.getAnotherDescription());
            statement.setString(10, entity.getAnotherLanguage());
            statement.setLong(11, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAmount(Book entity) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.UPDATE_AMOUNT_OF_BOOK)) {
            statement.setInt(1, entity.getCount());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setAuthorship(Book book, Author author) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.SET_AUTHORSHIP)) {
            statement.setLong(1, author.getId());
            statement.setLong(2, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unSetAuthorship(Book book, Author author) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.UNSET_AUTHORSHIP)) {
            statement.setLong(1, book.getId());
            statement.setLong(2, author.getId());
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

    private Book getBookWithAuthors(PreparedStatement getAuthorsStatement, ResultSet resultSet) throws SQLException {
        BookMapper bookMapper = new BookMapper();
        Book book = bookMapper.extractFromResultSetLocaled(resultSet);
        getAuthorsStatement.setLong(1, book.getId());
        List<Author> authors = new ArrayList<>();
        ResultSet resultSet1 = getAuthorsStatement.executeQuery();
        while (resultSet1.next()) {
            AuthorMapper authorMapper = new AuthorMapper();
            Author author = authorMapper.extractFromResultSetWithIdLocaled(resultSet1, "author_id");
            authors.add(author);
        }
        book.setAuthors(authors);
        return book;
    }

    private String chooseSortingQuery(String sortBy, String sortType) {
        if (LocalizationFilter.locale.getLanguage().equals("uk")) {
            return chooseSortingQueryUa(sortBy, sortType);
        } else {
            return chooseSortingQueryEn(sortBy, sortType);
        }
    }

    private String chooseSortingQueryUa(String sortBy, String sortType) {
        String query;
        if (sortType.equals("dec")) {
            if (sortBy.equals("title")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_TITLE_DEC_UA;
            } else if (sortBy.equals("author")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_AUTHOR_DEC_UA;
            } else if (sortBy.equals("edition")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_EDITION_DEC_UA;
            } else if (sortBy.equals("date")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_DATE_DEC_UA;
            } else {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_ID_DEC_UA;
            }
        } else {
            if (sortBy.equals("title")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_TITLE_INC_UA;
            } else if (sortBy.equals("author")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_AUTHOR_INC_UA;
            } else if (sortBy.equals("edition")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_EDITION_INC_UA;
            } else if (sortBy.equals("date")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_DATE_INC_UA;
            } else {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_ID_INC_UA;
            }
        }
        return query;
    }

    private String chooseSortingQueryEn(String sortBy, String sortType) {
        String query;
        if (sortType.equals("dec")) {
            if (sortBy.equals("title")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_TITLE_DEC_EN;
            } else if (sortBy.equals("author")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_AUTHOR_DEC_EN;
            } else if (sortBy.equals("edition")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_EDITION_DEC_EN;
            } else if (sortBy.equals("date")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_DATE_DEC_EN;
            } else {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_ID_DEC_EN;
            }
        } else {
            if (sortBy.equals("title")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_TITLE_INC_EN;
            } else if (sortBy.equals("author")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_AUTHOR_INC_EN;
            } else if (sortBy.equals("edition")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_EDITION_INC_EN;
            } else if (sortBy.equals("date")) {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_DATE_INC_EN;
            } else {
                query = SQLConstants.GET_PARTIAL_BOOKS_BY_KEYWORD_SORTED_BY_ID_INC_EN;
            }
        }
        return query;
    }
}
