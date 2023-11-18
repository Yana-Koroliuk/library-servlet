package ua.training.controller.command.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.training.controller.command.Command;
import ua.training.model.entity.Order;
import ua.training.model.entity.User;
import ua.training.model.service.OrderService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ReaderHome implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final OrderService orderService;
    private final UserService userService;

    public ReaderHome(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String userLogin = (String) request.getSession().getAttribute("userLogin");
        String orderId = request.getParameter("orderId");
        String tab = request.getParameter("tab");
        User user = userService.findByLogin(userLogin).orElseThrow(RuntimeException::new);
        if (orderId == null || orderId.equals("") || tab == null || tab.equals("")) {
            List<Order> orderList = orderService.findByUserId(user.getId());
            request.setAttribute("orderList", orderList);
            return "/user/reader/home.jsp";
        }
        boolean result = orderService.deleteOrder(Long.parseLong(orderId));
        if (!result) {
            logger.error("An error occurred when deleting order with id="+orderId);
            return "/error/error.jsp";
        }
        logger.info("Successfully deleted order with id="+orderId);
        List<Order> orderList = orderService.findByUserId(user.getId());
        request.setAttribute("orderList", orderList);
        return "/user/reader/home.jsp?tab="+tab;
    }
}
