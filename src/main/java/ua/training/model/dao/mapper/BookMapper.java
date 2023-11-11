package ua.training.model.dao.mapper;

import ua.training.controller.filters.LocalizationFilter;
import ua.training.model.entity.Book;
import ua.training.model.entity.Edition;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookMapper implements ObjectMapper<Book> {

    @Override
    public Book extractFromResultSet(ResultSet resultSet) throws SQLException {
        EditionMapper editionMapper = new EditionMapper();
        Edition edition = editionMapper.extractFromResultSetWithId(resultSet, "edition_id");
        return new Book.Builder()
                .id(resultSet.getInt("id"))
                .title(resultSet.getString("title_uk"))
                .anotherTitle(resultSet.getString("title_en"))
                .description(resultSet.getString("description_uk"))
                .anotherDescription(resultSet.getString("description_en"))
                .language(resultSet.getString("language_uk"))
                .anotherLanguage(resultSet.getString("language_en"))
                .edition(edition)
                .publicationDate(LocalDate.parse(resultSet.getString("publication_date")))
                .price(new BigDecimal(resultSet.getString("price_uan")))
                .count(resultSet.getInt("count"))
                .build();
    }

    public Book extractFromResultSetLocaled(ResultSet resultSet) throws SQLException {
        String language = LocalizationFilter.locale.getLanguage();
        Edition edition = new Edition.Builder()
                .id(resultSet.getLong("edition_id"))
                .name(resultSet.getString("edition_name_"+language))
                .build();
        BigDecimal price = new BigDecimal(resultSet.getString("price_uan"));
        if (language.equals("en")) {
            price = price.divide(new BigDecimal(30), BigDecimal.ROUND_CEILING);
        }
        return new Book.Builder()
                .id(resultSet.getInt("id"))
                .title(resultSet.getString("title_"+language))
                .description(resultSet.getString("description_"+language))
                .language(resultSet.getString("language_"+language))
                .edition(edition)
                .publicationDate(LocalDate.parse(resultSet.getString("publication_date")))
                .price(price)
                .count(resultSet.getInt("count"))
                .build();
    }
}
