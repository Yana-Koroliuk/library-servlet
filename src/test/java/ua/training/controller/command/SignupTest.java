package ua.training.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.User;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SignupTest {
    private UserService mockedUserService;
    private HttpServletRequest mockedRequest;
    private Signup signupCommand;

    @Before
    public void setUp() {
        mockedUserService = mock(UserService.class);
        mockedRequest = mock(HttpServletRequest.class);
        signupCommand = new Signup(mockedUserService);
    }

    @Test
    public void getSignupPage() {
        when(mockedRequest.getParameter("login")).thenReturn(null);
        when(mockedRequest.getParameter("password")).thenReturn("");

        String expected = "/signup.jsp";
        String actual = signupCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithInvalidInputData() {
        when(mockedRequest.getParameter("login")).thenReturn("34d");
        when(mockedRequest.getParameter("password")).thenReturn("ljjj");

        String expected = "/signup.jsp?validError=true";
        String actual = signupCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithAlreadyExistUser() {
        String login = "user1";
        User user = new User.Builder()
                .login(login)
                .build();
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn("11111eee");
        when(mockedUserService.findByLogin("user1")).thenReturn(Optional.of(user));

        String expected = "/signup.jsp?loginError=true";
        String actual = signupCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithSignUpFailure() {
        String login = "user1";
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn("11111eee");
        when(mockedUserService.findByLogin("user1")).thenReturn(Optional.empty());
        when(mockedUserService.singUpUser(any(User.class))).thenReturn(false);

        String expected = "/error/error.jsp";
        String actual = signupCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void execute() {
        String login = "user1";
        String password = "11111eee";
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn(password);
        when(mockedUserService.findByLogin("user1")).thenReturn(Optional.empty());
        when(mockedUserService.singUpUser(any(User.class))).thenReturn(true);

        String expected = "/signup.jsp?successEvent=true";
        String actual = signupCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }
}