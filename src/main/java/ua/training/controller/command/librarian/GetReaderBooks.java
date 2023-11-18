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
import java.util.Optional;
import java.util.stream.Collectors;

public class GetReaderBooks implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final OrderService orderService;
    private final UserService userService;

    public GetReaderBooks(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        if (userId == null || userId.equals("")) {
            return "/user/librarian/readerBooks.jsp";
        }
        Optional<User> optionalUser = userService.findById(Long.parseLong(userId));
        if (optionalUser.isPresent())  {
            User user = optionalUser.get();
            List<Order> orderList = orderService.findByUserId(Long.parseLong(userId)).stream()
                    .filter((order) -> order.getOrderStatus() != OrderStatus.APPROVED
                            || order.getOrderStatus() != OrderStatus.OVERDUE).collect(Collectors.toList());
            request.setAttribute("orderList", orderList);
            request.setAttribute("user", user);
            logger.info("Request for user subscriptions with id="+userId);
            return "/user/librarian/readerBooks.jsp";
        }
        return "/error/error.jsp";
    }
}
