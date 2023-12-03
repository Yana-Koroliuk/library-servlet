package ua.training.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.model.entity.enums.Role;
import ua.training.model.entity.User;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Pattern;

public class Signup implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public Signup(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (login == null  || password == null || login.equals("") || password.equals("")) {
            return "/signup.jsp";
        }
        String loginPattern = "^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{4,20}$";
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,30}$";
        boolean isLoginValid = Pattern.matches(loginPattern, login);
        boolean isPasswordValid = Pattern.matches(passwordPattern, password);
        if (!isLoginValid || !isPasswordValid) {
            return "redirect:/signup?validError=true";
        }
        Optional<User> optionalUser = userService.findByLogin(login);
        if (optionalUser.isPresent()) {
            return "redirect:/signup?loginError=true";
        }
        User user = new User.Builder()
                .login(login)
                .password_hash(password)
                .role(Role.READER)
                .isBlocked(false)
                .build();
        boolean result = userService.singUpUser(user);
        if (!result) {
            logger.error("An error occurred while registering user with login '"+login+"'");
            return "/error/error.jsp";
        }
        logger.info("User with login '"+login+"' successfully registered");
        return "redirect:/signup?successEvent=true";
    }
}
