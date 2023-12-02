package ua.training.controller.command.librarian;

import org.junit.Before;
import org.junit.Test;
import ua.training.controller.command.reader.OrderBook;
import ua.training.model.entity.Book;
import ua.training.model.entity.Order;
import ua.training.model.entity.User;
import ua.training.model.entity.enums.OrderStatus;
import ua.training.model.service.BookService;
import ua.training.model.service.OrderService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibrarianHomeTest {
    private UserService mockedUserService;
    private OrderService mockedOrderService;
    private HttpServletRequest mockedRequest;
    private LibrarianHome librarianHome;

    @Before
    public void setUp() {
        mockedUserService = mock(UserService.class);
        mockedOrderService = mock(OrderService.class);
        mockedRequest = mock(HttpServletRequest.class);
        librarianHome = new LibrarianHome(mockedOrderService, mockedUserService);
    }

    @Test
    public void getHomePage() {
        when(mockedRequest.getParameter("id")).thenReturn("");
        when(mockedRequest.getParameter("action")).thenReturn(null);
        when(mockedUserService.findAll()).thenReturn(Collections.singletonList(new User.Builder().build()));
        when(mockedOrderService.findAllWithStatus(OrderStatus.RECEIVED))
                .thenReturn(Collections.singletonList(new Order.Builder().build()));

        String expected = "/user/librarian/home.jsp";
        String actual = librarianHome.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeOrderApproval() {
        when(mockedRequest.getParameter("id")).thenReturn("1");
        when(mockedRequest.getParameter("action")).thenReturn("add");
        when(mockedUserService.findAll()).thenReturn(Collections.singletonList(new User.Builder().build()));
        when(mockedOrderService.findAllWithStatus(OrderStatus.RECEIVED))
                .thenReturn(Collections.singletonList(new Order.Builder().build()));
        when(mockedOrderService.approveOrder(1L)).thenReturn(true);

        String expected = "/user/librarian/home.jsp";
        String actual = librarianHome.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeOrderApprovalFailure() {
        when(mockedRequest.getParameter("id")).thenReturn("1");
        when(mockedRequest.getParameter("action")).thenReturn("add");
        when(mockedUserService.findAll()).thenReturn(Collections.singletonList(new User.Builder().build()));
        when(mockedOrderService.findAllWithStatus(OrderStatus.RECEIVED))
                .thenReturn(Collections.singletonList(new Order.Builder().build()));
        when(mockedOrderService.approveOrder(1L)).thenReturn(false);

        String expected = "/error/error.jsp";
        String actual = librarianHome.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeOrderCancelling() {
        when(mockedRequest.getParameter("id")).thenReturn("1");
        when(mockedRequest.getParameter("action")).thenReturn("delete");
        when(mockedUserService.findAll()).thenReturn(Collections.singletonList(new User.Builder().build()));
        when(mockedOrderService.findAllWithStatus(OrderStatus.RECEIVED))
                .thenReturn(Collections.singletonList(new Order.Builder().build()));
        when(mockedOrderService.cancelOrder(1L)).thenReturn(true);

        String expected = "/user/librarian/home.jsp";
        String actual = librarianHome.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeOrderCancellingFailure() {
        when(mockedRequest.getParameter("id")).thenReturn("1");
        when(mockedRequest.getParameter("action")).thenReturn("delete");
        when(mockedUserService.findAll()).thenReturn(Collections.singletonList(new User.Builder().build()));
        when(mockedOrderService.findAllWithStatus(OrderStatus.RECEIVED))
                .thenReturn(Collections.singletonList(new Order.Builder().build()));
        when(mockedOrderService.cancelOrder(1L)).thenReturn(false);

        String expected = "/error/error.jsp";
        String actual = librarianHome.execute(mockedRequest);

        assertEquals(expected, actual);
    }
}
