package ua.training.model.service;

import ua.training.model.dao.AuthorDao;
import ua.training.model.dao.BookDao;
import ua.training.model.dao.DaoFactory;
import ua.training.model.dao.EditionDao;
import ua.training.model.entity.Author;
import ua.training.model.entity.Book;
import ua.training.model.entity.Edition;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    public boolean createBook(Book book) {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            Edition edition = getEditionOrNew(book.getEdition());
            book.setEdition(edition);
            List<Author> authorList = book.getAuthors().stream().map(this::getAuthorOrNew).collect(Collectors.toList());
            book.setAuthors(authorList);
            bookDao.create(book);
            book.getAuthors().forEach((author) -> bookDao.setAuthorship(book, author));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBook(Book book) {
        try (BookDao bookDao = daoFactory.createBookDao();
            AuthorDao authorDao = daoFactory.createAuthorDao()) {
            Edition edition = getEditionOrNew(book.getEdition());
            book.setEdition(edition);
            List<Author> currAuthors = authorDao.getAuthorsByBookId(book.getId());
            List<Author> newAuthors = book.getAuthors();
            currAuthors.stream().filter((author -> !newAuthors.contains(author))).forEach((author) -> bookDao.unSetAuthorship(book, author));
            newAuthors.stream().filter((author -> !currAuthors.contains(author)))
                    .forEach((author -> bookDao.setAuthorship(book, getAuthorOrNew(author))));
            book.setAuthors(newAuthors);
            bookDao.update(book);
            return true;
        }
    }

    public boolean deleteBook(long id) {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            Optional<Book> optionalBook = bookDao.findById(id);
            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                bookDao.delete(book.getId());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Book> findByIdLocated(long id) {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return bookDao.findByIdWithLocaled(id);
        }
    }

    public Optional<Book> findById(long id) {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return bookDao.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Book> findAll() {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return bookDao.findAll();
        }
    }

    public List<Book> findAllByKeyWord(String keyWord, String sortBy, String sortType, int page) {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return bookDao.findByKeyWord(keyWord, sortBy, sortType, page);
        }
    }

    public Optional<Book> findByTitleAndAuthorsNames(String title, List<String> authorNames1, List<String> authorNames2) {
        try (AuthorDao authorDao = daoFactory.createAuthorDao();
            BookDao bookDao = daoFactory.createBookDao()) {
            List<Author> authors = new ArrayList<>();
            for (int i = 0; i < authorNames1.size(); i++) {
                Optional<Author> optionalAuthor = authorDao.findByNames(authorNames1.get(i), authorNames2.get(i));
                if (!optionalAuthor.isPresent()) {
                    return Optional.empty();
                } else {
                    authors.add(optionalAuthor.get());
                }
            }
            return bookDao.findByTitleAndAuthorsNames(title, authors);
        }
    }

    public Edition getEditionOrNew(Edition edition) {
        Edition result = null;
        try (EditionDao editionDao = daoFactory.createEditionDao()) {
            String name = edition.getName();
            String anotherName = edition.getAnotherName();
            Optional<Edition> optionalEdition = editionDao.findByNames(name, anotherName);
            if (optionalEdition.isPresent()) {
                result = optionalEdition.get();
            } else {
                optionalEdition = editionDao.create(edition);
                if (optionalEdition.isPresent()) {
                    result = edition;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Author getAuthorOrNew(Author author) {
        Author result = null;
        try (AuthorDao authorDao = daoFactory.createAuthorDao()) {
            String name = author.getName();
            String anotherName = author.getAnotherName();
            Optional<Author> optionalEdition = authorDao.findByNames(name, anotherName);
            if (optionalEdition.isPresent()) {
                result = optionalEdition.get();
            } else {
                optionalEdition = authorDao.create(author);
                if (optionalEdition.isPresent()) {
                    result = author;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public long getBookAmountWithKeyWord(String keyWord) {
        try (BookDao bookDao = daoFactory.createBookDao()) {
            return bookDao.getBookAmountWithKeyWord(keyWord);
        }
    }
}
