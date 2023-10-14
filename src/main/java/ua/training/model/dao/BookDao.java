package ua.training.model.dao;

import ua.training.model.entity.Author;
import ua.training.model.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao extends GenericDao<Book> {
    Optional<Book> findByTitleAndAuthorsNames(String title, List<Author> authors);
    void setAuthorship(Book book, Author author);
    void unSetAuthorship(Book book, Author author);
    List<Book> findByKeyWord(String keyWord, String sortBy, String sortType, int page);
    long getBookAmountWithKeyWord(String keyWord);

    Optional<Book> findByIdWithLocaled(long id);

    void updateAmount(Book book);
}
