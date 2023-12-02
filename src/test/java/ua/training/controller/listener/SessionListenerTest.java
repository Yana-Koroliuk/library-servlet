package ua.training.controller.listener;

import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.util.HashSet;

import static org.mockito.Mockito.*;

public class SessionListenerTest {

    @Test
    public void sessionDestroyed() {
        HttpSessionEvent event = mock(HttpSessionEvent.class);
        HttpSession session = mock(HttpSession.class);
        ServletContext context = mock(ServletContext.class);
        String login = "user";
        HashSet<String> loggedUsers = new HashSet<>();
        loggedUsers.add(login);
        when(event.getSession()).thenReturn(session);
        when(session.getServletContext()).thenReturn(context);
        when(context.getAttribute("loggedUsers")).thenReturn(loggedUsers);
        when(session.getAttribute("userLogin")).thenReturn(login);
        SessionListener listener = new SessionListener();

        listener.sessionDestroyed(event);
        loggedUsers.remove(login);

        verify(session, times(1)).setAttribute("loggedUsers", loggedUsers);
    }
}