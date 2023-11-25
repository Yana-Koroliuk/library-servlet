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

public class DeleteLibrarian implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;
    private final BookService bookService;

    public DeleteLibrarian(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String librarianId = request.getParameter("id");
        if (librarianId == null || librarianId.equals("")) {
            return "redirect:/admin/home?tab=1";
        }
        boolean result = userService.delete(Long.parseLong(librarianId));
        if (!result) {
            logger.error("An error occurred when deleting librarian with id="+librarianId);
            return "/error/error.jsp";
        }
        List<User> userList = userService.findAll();
        List<Book> bookList = bookService.findAll();
        request.setAttribute("userList", userList);
        request.setAttribute("bookList", bookList);
        logger.info("Deleted librarian with id="+librarianId);
        return "redirect:/admin/home?tab=1";
    }
}
