package ua.training.controller.command.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.command.Command;
import ua.training.model.entity.User;
import ua.training.model.entity.enums.Role;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddLibrarian implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public AddLibrarian(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        User user = new User.Builder()
                .login(login)
                .password_hash(password)
                .role(Role.LIBRARIAN)
                .isBlocked(false)
                .build();
        boolean result = userService.singUpUser(user);
        if (!result) {
            return "/error/error.jsp";
        } else {
            return "redirect:/admin/addLibrarian?successEvent=true";
        }
    }
}
