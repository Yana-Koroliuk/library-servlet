package ua.training.controller.command.reader;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.Book;
import ua.training.model.entity.Order;
import ua.training.model.entity.User;
import ua.training.model.service.BookService;
import ua.training.model.service.OrderService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderBookTest {
    private UserService mockedUserService;
    private BookService mockedBookService;
    private OrderService mockedOrderService;
    private HttpServletRequest mockedRequest;
    private User mockedUser;
    private Book mockedBook;
    private OrderBook orderBook;

    @Before
    public void setUp() {
        mockedUserService = mock(UserService.class);
        mockedBookService = mock(BookService.class);
        mockedOrderService = mock(OrderService.class);
        mockedRequest = mock(HttpServletRequest.class);
        mockedUser = mock(User.class);
        mockedBook = mock(Book.class);
        orderBook = new OrderBook(mockedUserService, mockedBookService, mockedOrderService);
    }

    @Test
    public void executeWithNoUser() {
        when(mockedRequest.getParameter("bookId")).thenReturn("1");
        when(mockedRequest.getParameter("userLogin")).thenReturn("");
        when(mockedRequest.getParameter("orderType")).thenReturn("");
        when(mockedRequest.getParameter("startDate")).thenReturn("");
        when(mockedRequest.getParameter("endDate")).thenReturn(null);
        when(mockedUserService.findByLogin(any())).thenReturn(Optional.empty());
        when(mockedBookService.findByIdLocated(1)).thenReturn(Optional.empty());

        String expected = "/error/error.jsp";
        String actual = orderBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }


    @Test(expected = NumberFormatException.class)
    public void executeWithNoBookId() {
        when(mockedRequest.getParameter("bookId")).thenReturn(null);
        when(mockedRequest.getParameter("userLogin")).thenReturn("");
        when(mockedRequest.getParameter("orderType")).thenReturn("");
        when(mockedRequest.getParameter("startDate")).thenReturn("");
        when(mockedRequest.getParameter("endDate")).thenReturn(null);
        when(mockedUserService.findByLogin(any())).thenReturn(Optional.empty());

        orderBook.execute(mockedRequest);
    }

    @Test
    public void executeWithNoBook() {
        when(mockedRequest.getParameter("bookId")).thenReturn("1");
        when(mockedRequest.getParameter("userLogin")).thenReturn("");
        when(mockedRequest.getParameter("orderType")).thenReturn("");
        when(mockedRequest.getParameter("startDate")).thenReturn("");
        when(mockedRequest.getParameter("endDate")).thenReturn(null);
        when(mockedUserService.findByLogin(any())).thenReturn(Optional.empty());
        when(mockedBookService.findByIdLocated(1)).thenReturn(Optional.empty());

        String expected = "/error/error.jsp";
        String actual = orderBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithNoOrderType() {
        when(mockedRequest.getParameter("bookId")).thenReturn("1");
        when(mockedRequest.getParameter("userLogin")).thenReturn("");
        when(mockedRequest.getParameter("orderType")).thenReturn("");
        when(mockedRequest.getParameter("startDate")).thenReturn("");
        when(mockedRequest.getParameter("endDate")).thenReturn(null);
        when(mockedUserService.findByLogin(any())).thenReturn(Optional.of(mockedUser));
        when(mockedBookService.findByIdLocated(1)).thenReturn(Optional.of(mockedBook));

        String expected = "/user/reader/orderBook.jsp";
        String actual = orderBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithZeroAmount() {
        when(mockedRequest.getParameter("bookId")).thenReturn("1");
        when(mockedRequest.getParameter("userLogin")).thenReturn("user");
        when(mockedRequest.getParameter("orderType")).thenReturn("subscription");
        when(mockedRequest.getParameter("startDate")).thenReturn("");
        when(mockedRequest.getParameter("endDate")).thenReturn(null);
        when(mockedUserService.findByLogin(any())).thenReturn(Optional.of(mockedUser));
        when(mockedBookService.findByIdLocated(1)).thenReturn(Optional.of(mockedBook));
        when(mockedBook.getCount()).thenReturn(0);

        String expected = "/user/reader/orderBook.jsp?amountError=true";
        String actual = orderBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }
}