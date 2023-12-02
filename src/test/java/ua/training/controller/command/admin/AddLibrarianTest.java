package ua.training.controller.command.admin;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.User;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddLibrarianTest {
    private UserService mockedUserService;
    private HttpServletRequest mockedRequest;
    private User mockedUser;
    private AddLibrarian addLibrarian;

    @Before
    public void setUp() {
        mockedUserService = mock(UserService.class);
        mockedRequest = mock(HttpServletRequest.class);
        mockedUser = mock(User.class);
        addLibrarian = new AddLibrarian(mockedUserService);
    }

    @Test
    public void getPage() {
        when(mockedRequest.getParameter("login")).thenReturn(null);
        when(mockedRequest.getParameter("password")).thenReturn("");

        String expected = "/user/admin/librarianForm.jsp";
        String actual = addLibrarian.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithInvalidLogin() {
        when(mockedRequest.getParameter("login")).thenReturn("12?ee");
        when(mockedRequest.getParameter("password")).thenReturn("111111eee");

        String expected = "/user/admin/librarianForm.jsp?validError=true";
        String actual = addLibrarian.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithInvalidPassword() {
        when(mockedRequest.getParameter("login")).thenReturn("user");
        when(mockedRequest.getParameter("password")).thenReturn("ee");

        String expected =  "/user/admin/librarianForm.jsp?validError=true";
        String actual = addLibrarian.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithAlreadyExistingLibrarian() {
        String login = "login";
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn("11111eee");
        when(mockedUserService.findByLogin(login)).thenReturn(Optional.of(mockedUser));

        String expected =  "/user/admin/librarianForm.jsp?loginError=true";
        String actual = addLibrarian.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithSignUpFailure() {
        String login = "login";
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn("11111eee");
        when(mockedUserService.findByLogin(login)).thenReturn(Optional.empty());
        when(mockedUserService.singUpUser(any(User.class))).thenReturn(false);

        String expected = "/error/error.jsp";
        String actual = addLibrarian.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void execute() {
        String login = "login";
        when(mockedRequest.getParameter("login")).thenReturn(login);
        when(mockedRequest.getParameter("password")).thenReturn("11111eee");
        when(mockedUserService.findByLogin(login)).thenReturn(Optional.empty());
        when(mockedUserService.singUpUser(any(User.class))).thenReturn(true);

        String expected = "/user/admin/librarianForm.jsp?successEvent=true";
        String actual = addLibrarian.execute(mockedRequest);

        assertEquals(expected, actual);
    }
}
