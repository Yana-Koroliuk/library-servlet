package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.enums.Role;
import ua.training.model.entity.User;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Pattern;

public class Login implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public Login(UserService userService) {
        this.userService = userService;
    }


    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (login == null || login.equals("") || password == null || password.equals("")) {
            return "/login.jsp";
        }
        String loginPattern = "^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{4,20}$";
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,30}$";
        boolean isLoginValid = Pattern.matches(loginPattern, login);
        boolean isPasswordValid = Pattern.matches(passwordPattern, password);
        if (!isLoginValid || !isPasswordValid) {
            return "redirect:/login?validError=true";
        }
        Optional<User> optionalUser = userService.findByLogin(login);
        if (!optionalUser.isPresent()) {
            return "redirect:/login?loginError=true";
        }
        User user = optionalUser.get();
        if (!password.equals(user.getPasswordHash())) {
            return "redirect:/login?passwordError=true";
        }
        if (user.isBlocked()) {
            logger.info("An attempt was made to login to a blocked account");
            return "/blocked.jsp";
        }
        if (CommandUtility.checkUserIsLogged(request, login)) {
            logger.info("User '"+login+"' already logged in");
            return "/error/error.jsp";
        }
        if (user.getRole().equals(Role.READER)) {
            CommandUtility.setUserRole(request, Role.READER, login);
            logger.info("Successfully login user '"+login+"' as reader");
            return "redirect:/reader/home";
        } else if (user.getRole().equals(Role.LIBRARIAN)) {
            CommandUtility.setUserRole(request, Role.LIBRARIAN, login);
            logger.info("Successfully login user '"+login+"' as librarian");
            return "redirect:/librarian/home";
        } else if (user.getRole().equals(Role.ADMIN)) {
            CommandUtility.setUserRole(request, Role.ADMIN, login);
            logger.info("Successfully login user '"+login+"' as admin");
            return "redirect:/admin/home";
        } else {
            return "redirect:/login?loginError=true";
        }
    }
}
