package ua.training.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.Book;
import ua.training.model.service.BookService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchTest {
    private BookService mockedBookService;
    private HttpServletRequest mockedRequest;
    private Search searchCommand;

    @Before
    public void setUp() {
        mockedBookService = mock(BookService.class);
        mockedRequest = mock(HttpServletRequest.class);
        searchCommand = new Search(mockedBookService);
    }

    @Test
    public void getSearchFirstPage() {
        when(mockedRequest.getParameter("keyWords")).thenReturn("");
        when(mockedRequest.getParameter("page")).thenReturn("");
        when(mockedRequest.getParameter("sortBy")).thenReturn("");
        when(mockedRequest.getParameter("sortType")).thenReturn("");

        String expected = "/search?page=1";
        String actual = searchCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithNoSortTypeWhenSortByGiven() {
        String keyWords = "word";
        when(mockedRequest.getParameter("keyWords")).thenReturn(keyWords);
        when(mockedRequest.getParameter("page")).thenReturn("1");
        when(mockedRequest.getParameter("sortBy")).thenReturn("title");
        when(mockedRequest.getParameter("sortType")).thenReturn("");

        String expected = "/search?page=1&sortBy=id&sortType=inc&keyWords="+keyWords;
        String actual = searchCommand.execute(mockedRequest);

        assertEquals(expected, actual);
    }

}