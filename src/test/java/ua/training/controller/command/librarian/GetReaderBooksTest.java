package ua.training.controller.command.librarian;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.Order;
import ua.training.model.entity.User;
import ua.training.model.service.OrderService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetReaderBooksTest {
    private UserService mockedUserService;
    private OrderService mockedOrderService;
    private HttpServletRequest mockedRequest;
    private User mockedUser;
    private GetReaderBooks getReaderBooks;

    @Before
    public void setUp() {
        mockedUserService = mock(UserService.class);
        mockedOrderService = mock(OrderService.class);
        mockedRequest = mock(HttpServletRequest.class);
        mockedUser = mock(User.class);
        getReaderBooks = new GetReaderBooks(mockedOrderService, mockedUserService);
    }

    @Test
    public void getReaderBooksPage() {
        when(mockedRequest.getParameter("userId")).thenReturn(null);

        String expected = "/user/librarian/readerBooks.jsp";
        String actual = getReaderBooks.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithNoUser() {
        when(mockedRequest.getParameter("userId")).thenReturn("1");
        when(mockedUserService.findById(1L)).thenReturn(Optional.empty());

        String expected = "/error/error.jsp";
        String actual = getReaderBooks.execute(mockedRequest);

        assertEquals(expected, actual);
    }


    @Test
    public void execute() {
        when(mockedRequest.getParameter("userId")).thenReturn("1");
        when(mockedUserService.findById(1L)).thenReturn(Optional.of(mockedUser));
        when(mockedOrderService.findByUserId(1L)).thenReturn(Collections.singletonList(new Order.Builder().build()));

        String expected = "/user/librarian/readerBooks.jsp";
        String actual = getReaderBooks.execute(mockedRequest);

        assertEquals(expected, actual);
    }
}
