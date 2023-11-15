package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.Book;
import ua.training.model.service.BookService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class Search implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final BookService bookService;

    public Search(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String keyWords = request.getParameter("keyWords").trim();
        String pageString = request.getParameter("page");
        String sortBy = request.getParameter("sortBy");
        String sortType = request.getParameter("sortType");
        List<Book> bookList;
        if (pageString == null || pageString.equals("")) {
            return "redirect:/search?page=1";
        }
        int page = Integer.parseInt(pageString.trim());
        if ((sortBy != null && !sortBy.equals("")) && (sortType == null || sortType.equals(""))) {
            return "redirect:/search?page=1&sortBy=id&sortType=inc&keyWords="+keyWords;
        }
        if (sortBy == null) {
            sortBy = "id";
        }
        if (sortType == null) {
            sortType = "free";
        }
        bookList = bookService.findAllByKeyWord(keyWords, sortBy, sortType, page);
        long amountOfPages = (bookService.getBookAmountWithKeyWord(keyWords)-1)/4+1;
        request.setAttribute("bookList", bookList);
        logger.info(String.format("Search with parameters {page:%s, sortBy:%s, sortType:%s, keyWords:%s, amountOfPages:%s}", page, sortBy, sortType, keyWords, amountOfPages));
        return "/search.jsp?page="+pageString+"&sortBy="+sortBy+"&sortType="+sortType+"&keyWords="+keyWords+"&amountOfPages="+amountOfPages;
    }
}
