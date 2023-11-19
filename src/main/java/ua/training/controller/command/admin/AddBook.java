package ua.training.controller.command.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.command.Command;
import ua.training.model.entity.Author;
import ua.training.model.entity.Book;
import ua.training.model.entity.Edition;
import ua.training.model.service.BookService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AddBook implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final BookService bookService;

    public AddBook(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("action", "add");

        String titleUa = request.getParameter("titleUa");
        String authorsStringUa = request.getParameter("authorsUa");
        String descriptionUa = request.getParameter("descriptionUa");
        String languageUa = request.getParameter("bookLanguageUa");
        String editionNameUa = request.getParameter("editionUa");
        String titleEn = request.getParameter("titleEn");
        String authorsStringEn = request.getParameter("authorsEn");
        String descriptionEn = request.getParameter("descriptionEn");
        String languageEn = request.getParameter("bookLanguageEn");
        String editionNameEn = request.getParameter("editionEn");
        String publicationDateString = request.getParameter("publicationDate");
        String stringPrice = request.getParameter("price");
        String currency = request.getParameter("currency");
        String stringCount = request.getParameter("count");

        List<String> params = Arrays.asList(titleUa, titleEn, authorsStringUa, authorsStringEn, descriptionUa, descriptionEn,
                languageUa, languageEn, editionNameUa, editionNameEn, currency, stringPrice, stringCount, publicationDateString);
        for (String param : params) {
            if (param == null || param.equals("")) {
                return "/user/admin/bookForm.jsp";
            }
        }
        LocalDate publicationData = LocalDate.parse(publicationDateString);
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(stringPrice));
        int count = Integer.parseInt(stringCount);
        boolean condition5 = publicationData.isAfter(LocalDate.now()) || publicationData.isEqual(LocalDate.now());
        boolean condition6 = price.compareTo(BigDecimal.ZERO) <= 0 || count <= 0;
        if (condition5 || condition6) {
            return "redirect:/admin/addBook?validError=true";
        }
        List<String> authorNamesUa = Arrays.asList(authorsStringUa.split(","));
        List<String> authorNamesEn = Arrays.asList(authorsStringEn.split(","));
        BigDecimal priceUa;
        if (currency.equals("uan")) {
            priceUa = price;
        } else {
            priceUa = price.multiply(new BigDecimal(30));
        }
        Optional<Book> optionalBook = bookService.findByTitleAndAuthorsNames(titleUa, authorNamesUa, authorNamesEn);
        if (optionalBook.isPresent()) {
            return "redirect:/admin/addBook?createError=true";
        }

        Edition edition = new Edition.Builder()
                .name(editionNameUa)
                .anotherName(editionNameEn)
                .build();

        List<Author> authors = new ArrayList<>();
        for (int i = 0; i < authorNamesUa.size(); i++) {
            Author author = new Author.Builder()
                    .name(authorNamesUa.get(i))
                    .anotherName(authorNamesEn.get(i))
                    .build();
            authors.add(author);
        }

        Book book = new Book.Builder()
                .title(titleUa)
                .anotherTitle(titleEn)
                .description(descriptionUa)
                .anotherDescription(descriptionEn)
                .language(languageUa)
                .anotherLanguage(languageEn)
                .edition(edition)
                .publicationDate(publicationData)
                .price(priceUa)
                .count(count)
                .authors(authors)
                .build();

        boolean result = bookService.createBook(book);
        if (!result) {
            logger.error("An error occurred when creating book with title="+titleUa+"/"+titleEn+"" +
                    " and authors="+authorsStringUa+"/"+authorsStringEn);
            return "/error/error.jsp";
        }
        logger.info("Created book with title="+titleUa+"/"+titleEn+" and authors="+authorsStringUa+"/"+authorsStringEn);
        return "redirect:/admin/addBook?successCreation=true";
    }
}
