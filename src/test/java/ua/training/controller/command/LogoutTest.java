package ua.training.controller.command;

import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogoutTest {

    @Test
    public void execute() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        ServletContext context = mock(ServletContext.class);
        HashSet<String> loggedUsers = new HashSet<>();
        loggedUsers.add("user1");
        loggedUsers.add("user2");
        when(request.getSession()).thenReturn(session);
        when(session.getServletContext()).thenReturn(context);
        when(context.getAttribute("loggedUsers")).thenReturn(loggedUsers);
        when(request.getSession().getAttribute("userLogin")).thenReturn("user1");
        Logout logoutCommand = new Logout();

        String expected = "redirect:/index.jsp";
        String actual = logoutCommand.execute(request);

        assertEquals(expected, actual);

    }
}