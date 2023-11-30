package ua.training.controller.command.reader;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.Order;
import ua.training.model.entity.User;
import ua.training.model.service.OrderService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReaderHomeTest {
    private UserService mockedUserService;
    private OrderService mockedOrderService;
    private HttpServletRequest mockedRequest;
    private HttpSession mockedSession;
    private ReaderHome readerHome;
    private User mockedUser;

    @Before
    public void setUp() {
        mockedUserService = mock(UserService.class);
        mockedOrderService = mock(OrderService.class);
        mockedRequest = mock(HttpServletRequest.class);
        mockedSession = mock(HttpSession.class);
        mockedUser = mock(User.class);
        readerHome = new ReaderHome(mockedOrderService, mockedUserService);
    }

    @Test
    public void getReaderHomePage() {
        long userId = 1;
        when(mockedRequest.getSession()).thenReturn(mockedSession);
        when(mockedSession.getAttribute("userLogin")).thenReturn("user");
        when(mockedRequest.getParameter("orderId")).thenReturn(null);
        when(mockedRequest.getParameter("tab")).thenReturn(null);
        when(mockedUserService.findByLogin("user")).thenReturn(Optional.of(mockedUser));
        when(mockedUser.getId()).thenReturn(userId);
        when(mockedOrderService.findByUserId(1)).thenReturn(Collections.singletonList(new Order.Builder().build()));

        String expected = "/user/reader/home.jsp";
        String actual = readerHome.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void getReaderHomePageWithNoUser() {
        when(mockedRequest.getSession()).thenReturn(mockedSession);
        when(mockedSession.getAttribute("userLogin")).thenReturn("user");
        when(mockedRequest.getParameter("orderId")).thenReturn(null);
        when(mockedRequest.getParameter("tab")).thenReturn(null);
        when(mockedUserService.findByLogin("user")).thenReturn(Optional.empty());

        readerHome.execute(mockedRequest);
    }

}
