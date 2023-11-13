package ua.training.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.command.*;
import ua.training.model.service.BookService;
import ua.training.model.service.OrderService;
import ua.training.model.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *  Class of the main servlet of the project
 *
 **/
public class Servlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    private final Map<String, Command> commandMap = new HashMap<>();

    public void init() {
        commandMap.put("signup", new Signup(new UserService()));
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        logger.info("Received request uri: "+path);
        path = path.replaceAll(".*/app/" , "");
        Command command = commandMap.getOrDefault(path, (r) -> "/index.jsp");
        String page = command.execute(request);
        if (page.contains("redirect:")) {
            String newPath = page.replace("redirect:", "/app");
            logger.info("Redirect to: "+newPath);
            response.sendRedirect(newPath);
        } else {
            logger.info("Forward to: "+page);
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}
