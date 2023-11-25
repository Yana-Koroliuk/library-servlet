package ua.training.controller.command.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.command.Command;
import ua.training.model.entity.User;
import ua.training.model.service.UserService;

import javax.management.openmbean.OpenDataException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;

public class BlockUser implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService;

    public BlockUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String userId = request.getParameter("id");
        if (userId == null || userId.equals("")) {
            return "/error/error.jsp";
        }
        Optional<User> optionalUser = userService.findById(Long.parseLong(userId));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean result = userService.blockUser(user);
            if (!result) {
                logger.error("An error occurred when blocking user with id="+userId);
                return "/error/error.jsp";
            } else {
                HashSet<String> loggedUsers = (HashSet<String>) request.getSession().getServletContext().getAttribute("loggedUsers");
                if (loggedUsers != null) {
                    loggedUsers.remove(user.getLogin());
                }
                request.getSession().getServletContext().setAttribute("loggedUsers", loggedUsers);
                logger.info("Blocked user with id="+userId);
                return "redirect:/admin/home";
            }
        }
        return "/error/error.jsp";
    }
}
