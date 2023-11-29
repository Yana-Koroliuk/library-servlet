package ua.training.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.User;
import ua.training.model.entity.enums.Role;
import ua.training.model.service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginTest {
    private HttpServletRequest mockedRequest;
    private UserService mockedUserService;
    private User mockedUser;
    private Login loginCommand;

    @Before
    public void setUp() {
        mockedRequest = mock(HttpServletRequest.class);
        mockedUserService = mock(UserService.class);
        mockedUser = mock(User.class);
        loginCommand = new Login(mockedUserService);
    }

    @Test
    public void getLoginPage() {
        when(mockedRequest.getParameter("login")).thenReturn(null);
        when(mockedRequest.getParameter("password")).thenReturn("");

        String expected = "/login.jsp";
        String actual = loginCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithInvalidInputData() {
        when(mockedRequest.getParameter("login")).thenReturn("34d");
        when(mockedRequest.getParameter("password")).thenReturn("ljjj");

        String expected = "/login.jsp?validError=true";
        String actual = loginCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithAlreadyExistUser() {
        String login = "user1";
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn("11111eee");
        when(mockedUserService.findByLogin("user1")).thenReturn(Optional.empty());

        String expected = "/login.jsp?loginError=true";
        String actual = loginCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithWrongPassword() {
        String login = "user1";
        String password = "11111eee";
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn(password);
        when(mockedUserService.findByLogin("user1")).thenReturn(Optional.of(mockedUser));
        when(mockedUser.getPasswordHash()).thenReturn("12ed2d");

        String expected = "/login.jsp?passwordError=true";
        String actual = loginCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithBlockedUser() {
        String login = "user1";
        String password = "11111eee";
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn(password);
        when(mockedUserService.findByLogin("user1")).thenReturn(Optional.of(mockedUser));
        when(mockedUser.getPasswordHash()).thenReturn(password);
        when(mockedUser.isBlocked()).thenReturn(true);

        String expected = "/blocked.jsp";
        String actual = loginCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithAlreadyLoggedInUser() {
        HttpSession session = mock(HttpSession.class);
        ServletContext context = mock(ServletContext.class);
        String login = "user1";
        String password = "11111eee";
        HashSet<String> loggedUsers = new HashSet<>();
        loggedUsers.add(login);
        loggedUsers.add("user2");
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn(password);
        when(mockedUserService.findByLogin("user1")).thenReturn(Optional.of(mockedUser));
        when(mockedUser.getPasswordHash()).thenReturn(password);
        when(mockedUser.isBlocked()).thenReturn(false);
        when(mockedUser.getRole()).thenReturn(Role.ADMIN);
        when(mockedRequest.getSession()).thenReturn(session);
        when(session.getServletContext()).thenReturn(context);
        when(context.getAttribute("loggedUsers")).thenReturn(loggedUsers);

        String expected = "/error/error.jsp";
        String actual = loginCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void execute() {
        HttpSession session = mock(HttpSession.class);
        ServletContext context = mock(ServletContext.class);
        String login = "user1";
        String password = "11111eee";
        HashSet<String> loggedUsers = new HashSet<>();
        loggedUsers.add("user2");
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn(password);
        when(mockedUserService.findByLogin("user1")).thenReturn(Optional.of(mockedUser));
        when(mockedUser.getPasswordHash()).thenReturn(password);
        when(mockedUser.isBlocked()).thenReturn(false);
        when(mockedUser.getRole()).thenReturn(Role.ADMIN);
        when(mockedRequest.getSession()).thenReturn(session);
        when(session.getServletContext()).thenReturn(context);
        when(context.getAttribute("loggedUsers")).thenReturn(loggedUsers);

        String expected = "redirect:/admin/home";
        String actual = loginCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }
}