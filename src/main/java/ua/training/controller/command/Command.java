package ua.training.controller.command;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Interface that represents the user command
 *
 */
public interface Command {
    /**
     * Method that executes command
     * @param request - input servlet request
     * @return - page
     */
    String execute(HttpServletRequest request);
}
