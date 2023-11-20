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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EditBook implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final BookService bookService;

    public EditBook(BookService bookService) {
        this.bookService = bookService;
    }


    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("action", "edit");

        String id = request.getParameter("id");
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
                Optional<Book> optionalBook1 = bookService.findById(Long.parseLong(id));
                optionalBook1.ifPresent(value -> request.setAttribute("book", value));
                return "/user/admin/bookForm.jsp?id="+id;
            }
        }


        Optional<Book> optionalBook1 = bookService.findById(Long.parseLong(id));
        optionalBook1.ifPresent(value -> request.setAttribute("book", value));
        return "redirect:/admin/editBook?id="+id+"&successCreation=true";
    }
}
