package ua.training.controller.command;

import ua.training.model.entity.enums.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;

/**
 *
 *  The class that provides the authorization process
 *
 */
public class CommandUtility {

    /**
     * A method that checks whether the user is already authorized,
     * if not then adds it to the logged in users
     * @param request - input servlet request
     * @param login - user login
     * @return true - when user is logged
     *         false - otherwise
     */
    public static boolean checkUserIsLogged(HttpServletRequest request, String login) {
        HashSet<String> loggedUsers = (HashSet<String>) request.getSession().getServletContext().getAttribute("loggedUsers");
        if (loggedUsers == null) {
            loggedUsers = new HashSet<>();
        }
        if (loggedUsers.stream().anyMatch(login::equals)) {
            return true;
        }
        loggedUsers.add(login);
        request.getSession().getServletContext().setAttribute("loggedUsers", loggedUsers);
        return false;
    }

    /**
     * A method that stores user credentials in a session
     * @param request - input servlet request
     * @param role - user role
     * @param login - user login
     */
    public static void setUserRole(HttpServletRequest request, Role role, String login) {
        HttpSession session = request.getSession();
        session.setAttribute("userLogin", login);
        session.setAttribute("role", role);
    }
}
