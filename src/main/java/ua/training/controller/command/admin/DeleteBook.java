package ua.training.controller.command.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.command.Command;
import ua.training.model.entity.Book;
import ua.training.model.entity.User;
import ua.training.model.service.BookService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class DeleteBook implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;
    private final BookService bookService;

    public DeleteBook(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String bookId = request.getParameter("id");
        if (bookId == null || bookId.equals("")) {
            return "redirect:/admin/home?tab=2";
        }
        boolean result = bookService.deleteBook(Long.parseLong(bookId));
        if (!result) {
            logger.error("An error occurred when deleting book with id="+bookId);
            return "/error/error.jsp";
        }
        logger.info("Deleted book with id="+bookId);
        return "redirect:/admin/home?tab=2";
    }
}
