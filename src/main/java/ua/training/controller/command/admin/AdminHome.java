package ua.training.controller.command.admin;

import ua.training.controller.command.Command;
import ua.training.model.entity.Book;
import ua.training.model.entity.User;
import ua.training.model.service.BookService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AdminHome implements Command {
    private final UserService userService;
    private final BookService bookService;

    public AdminHome(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String tab = request.getParameter("tab");
        List<User> userList = userService.findAll();
        List<Book> bookList = bookService.findAll();
        request.setAttribute("userList", userList);
        request.setAttribute("bookList", bookList);
        if (tab == null || tab.equals("")) {
            return "/user/admin/home.jsp";
        }
        return "/user/admin/home.jsp?tab="+tab;
    }
}
