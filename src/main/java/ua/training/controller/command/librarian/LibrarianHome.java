package ua.training.controller.command.librarian;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.command.Command;
import ua.training.model.entity.Order;
import ua.training.model.entity.User;
import ua.training.model.entity.enums.OrderStatus;
import ua.training.model.service.OrderService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LibrarianHome implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final OrderService orderService;
    private final UserService userService;

    public LibrarianHome(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String orderId = request.getParameter("id");
        String action = request.getParameter("action");
        if (orderId == null || orderId.equals("") || action == null || action.equals("")) {
            List<User> userList = userService.findAll();
            List<Order> orderList = orderService.findAllWithStatus(OrderStatus.RECEIVED);
            request.setAttribute("orderList", orderList);
            request.setAttribute("userList", userList);
            return "/user/librarian/home.jsp";
        }
        if (action.equals("add")) {
            boolean result = orderService.approveOrder(Long.parseLong(orderId));
            if (!result) {
                logger.error("An error has occurred when approving order with id="+orderId);
                return "/error/error.jsp";
            }
            logger.info("Order with id="+orderId+" is approved");
        }
        if (action.equals("delete")) {
            boolean result = orderService.cancelOrder(Long.parseLong(orderId));
            if (!result) {
                logger.error("An error occurred when canceling order with id="+orderId);
                return "/error/error.jsp";
            }
            logger.info("Canceled order with id="+orderId);
        }
        List<User> userList = userService.findAll();
        List<Order> orderList = orderService.findAllWithStatus(OrderStatus.RECEIVED);
        request.setAttribute("orderList", orderList);
        request.setAttribute("userList", userList);
        return "/user/librarian/home.jsp";
    }
}
