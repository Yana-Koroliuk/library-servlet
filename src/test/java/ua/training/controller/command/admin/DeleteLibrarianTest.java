package ua.training.controller.command.admin;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.service.BookService;
import ua.training.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteLibrarianTest {
    private UserService mockedUserService;
    private BookService mockedBookService;
    private HttpServletRequest mockedRequest;
    private DeleteLibrarian deleteLibrarian;

    @Before
    public void setUp() {
        mockedUserService = mock(UserService.class);
        mockedBookService = mock(BookService.class);
        mockedRequest = mock(HttpServletRequest.class);
        deleteLibrarian = new DeleteLibrarian(mockedUserService, mockedBookService);
    }

    @Test
    public void executeWithInvalidInputData() {
        when(mockedRequest.getParameter("id")).thenReturn(null);

        String expected = "/user/admin/home.jsp";
        String actual = deleteLibrarian.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithDeleteFailure() {
        String librarianId = "1";
        when(mockedRequest.getParameter("id")).thenReturn(librarianId);
        when(mockedUserService.delete(Long.parseLong(librarianId))).thenReturn(false);

        String expected = "/error/error.jsp";
        String actual = deleteLibrarian.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void execute() {
        String librarianId = "1";
        when(mockedRequest.getParameter("id")).thenReturn(librarianId);
        when(mockedUserService.delete(Long.parseLong(librarianId))).thenReturn(true);
        when(mockedUserService.findAll()).thenReturn(Collections.emptyList());
        when(mockedBookService.findAll()).thenReturn(Collections.emptyList());

        String expected = "/user/admin/home.jsp";
        String actual = deleteLibrarian.execute(mockedRequest);

        assertEquals(expected, actual);
    }
}
