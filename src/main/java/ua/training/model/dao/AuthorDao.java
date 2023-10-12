package ua.training.model.dao;

import ua.training.model.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao extends GenericDao<Author> {
    Optional<Author> findByNames(String name1, String name2);
    List<Author> getAuthorsByBookId(long id);
}
