package ua.training.controller.command.admin;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.User;
import ua.training.model.service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnBlockUserTest {
    private UserService mockedUserService;
    private HttpServletRequest mockedRequest;
    private User mockedUser;
    private UnBlockUser unBlockUser;

    @Before
    public void setUp() {
        mockedUserService = mock(UserService.class);
        mockedRequest = mock(HttpServletRequest.class);
        mockedUser = mock(User.class);
        unBlockUser = new UnBlockUser(mockedUserService);
    }

    @Test
    public void executeWithInvalidInputData() {
        when(mockedRequest.getParameter("id")).thenReturn(null);

        String expected = "/error/error.jsp";
        String actual = unBlockUser.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithNoExistingUser() {
        when(mockedRequest.getParameter("id")).thenReturn("1");
        when(mockedUserService.findById(1L)).thenReturn(Optional.empty());

        String expected = "/error/error.jsp";
        String actual = unBlockUser.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithBlockFailure() {
        when(mockedRequest.getParameter("id")).thenReturn("1");
        when(mockedUserService.findById(1L)).thenReturn(Optional.of(mockedUser));
        when(mockedUserService.blockUser(mockedUser)).thenReturn(false);

        String expected = "/error/error.jsp";
        String actual = unBlockUser.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void execute() {
        HttpSession session = mock(HttpSession.class);
        ServletContext context = mock(ServletContext.class);
        String login = "login";
        HashSet<String> loggedUsers = new HashSet<>();
        loggedUsers.add(login);
        when(mockedRequest.getParameter("id")).thenReturn("1");
        when(mockedUserService.findById(1L)).thenReturn(Optional.of(mockedUser));
        when(mockedUserService.unBlockUser(mockedUser)).thenReturn(true);
        when(mockedRequest.getSession()).thenReturn(session);
        when(session.getServletContext()).thenReturn(context);
        when(context.getAttribute("loggedUsers")).thenReturn(loggedUsers);
        when(mockedUser.getLogin()).thenReturn(login);

        String expected = "redirect:/admin/home";
        String actual = unBlockUser.execute(mockedRequest);

        assertEquals(expected, actual);
    }
}
