package ua.training.controller.command.admin;

import org.junit.Before;
import org.junit.Test;
import ua.training.model.entity.Book;
import ua.training.model.service.BookService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddBookTest {
    private BookService mockedBookService;
    private HttpServletRequest mockedRequest;
    private Book mockedBook;
    private AddBook addBook;

    @Before
    public void setUp() {
        mockedBookService = mock(BookService.class);
        mockedRequest = mock(HttpServletRequest.class);
        mockedBook = mock(Book.class);
        addBook = new AddBook(mockedBookService);
    }

    @Test
    public void executeWithInvalidData() {
        when(mockedRequest.getParameter("titleUa")).thenReturn("");
        when(mockedRequest.getParameter("authorsUa")).thenReturn("Автор");
        when(mockedRequest.getParameter("descriptionUa")).thenReturn("Опис");
        when(mockedRequest.getParameter("bookLanguageUa")).thenReturn("Українська");
        when(mockedRequest.getParameter("editionUa")).thenReturn("Видання");
        when(mockedRequest.getParameter("titleEn")).thenReturn("");
        when(mockedRequest.getParameter("authorsEn")).thenReturn("");
        when(mockedRequest.getParameter("descriptionEn")).thenReturn(null);
        when(mockedRequest.getParameter("bookLanguageEn")).thenReturn("Ukrainian");
        when(mockedRequest.getParameter("editionEn")).thenReturn("Edition");
        when(mockedRequest.getParameter("publicationDate")).thenReturn("");
        when(mockedRequest.getParameter("price")).thenReturn("100.07");
        when(mockedRequest.getParameter("currency")).thenReturn("uan");
        when(mockedRequest.getParameter("count")).thenReturn("7");

        String expected = "/user/admin/bookForm.jsp";
        String actual = addBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithInvalidNumeric() {
        when(mockedRequest.getParameter("titleUa")).thenReturn("Заголовок");
        when(mockedRequest.getParameter("authorsUa")).thenReturn("Автор");
        when(mockedRequest.getParameter("descriptionUa")).thenReturn("Опис");
        when(mockedRequest.getParameter("bookLanguageUa")).thenReturn("Українська");
        when(mockedRequest.getParameter("editionUa")).thenReturn("Видання");
        when(mockedRequest.getParameter("titleEn")).thenReturn("Title");
        when(mockedRequest.getParameter("authorsEn")).thenReturn("Author");
        when(mockedRequest.getParameter("descriptionEn")).thenReturn("Description");
        when(mockedRequest.getParameter("bookLanguageEn")).thenReturn("Ukrainian");
        when(mockedRequest.getParameter("editionEn")).thenReturn("Edition");
        when(mockedRequest.getParameter("publicationDate")).thenReturn("1986-12-12");
        when(mockedRequest.getParameter("price")).thenReturn("0");
        when(mockedRequest.getParameter("currency")).thenReturn("uan");
        when(mockedRequest.getParameter("count")).thenReturn("7");

        String expected = "/user/admin/bookForm.jsp?validError=true";
        String actual = addBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithInvalidPublicationDate() {
        when(mockedRequest.getParameter("titleUa")).thenReturn("Заголовок");
        when(mockedRequest.getParameter("authorsUa")).thenReturn("Автор");
        when(mockedRequest.getParameter("descriptionUa")).thenReturn("Опис");
        when(mockedRequest.getParameter("bookLanguageUa")).thenReturn("Українська");
        when(mockedRequest.getParameter("editionUa")).thenReturn("Видання");
        when(mockedRequest.getParameter("titleEn")).thenReturn("Title");
        when(mockedRequest.getParameter("authorsEn")).thenReturn("Author");
        when(mockedRequest.getParameter("descriptionEn")).thenReturn("Description");
        when(mockedRequest.getParameter("bookLanguageEn")).thenReturn("Ukrainian");
        when(mockedRequest.getParameter("editionEn")).thenReturn("Edition");
        when(mockedRequest.getParameter("publicationDate")).thenReturn("2222-12-12");
        when(mockedRequest.getParameter("price")).thenReturn("10");
        when(mockedRequest.getParameter("currency")).thenReturn("uan");
        when(mockedRequest.getParameter("count")).thenReturn("7");

        String expected = "/user/admin/bookForm.jsp?validError=true";
        String actual = addBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithAlreadyExistBook() {
        String titleUa = "Заголовок";
        List<String> authorNamesUa = Collections.singletonList("Автор");
        List<String> authorNamesEN = Collections.singletonList("Author");
        when(mockedRequest.getParameter("titleUa")).thenReturn(titleUa);
        when(mockedRequest.getParameter("authorsUa")).thenReturn("Автор");
        when(mockedRequest.getParameter("descriptionUa")).thenReturn("Опис");
        when(mockedRequest.getParameter("bookLanguageUa")).thenReturn("Українська");
        when(mockedRequest.getParameter("editionUa")).thenReturn("Видання");
        when(mockedRequest.getParameter("titleEn")).thenReturn("Title");
        when(mockedRequest.getParameter("authorsEn")).thenReturn("Author");
        when(mockedRequest.getParameter("descriptionEn")).thenReturn("Description");
        when(mockedRequest.getParameter("bookLanguageEn")).thenReturn("Ukrainian");
        when(mockedRequest.getParameter("editionEn")).thenReturn("Edition");
        when(mockedRequest.getParameter("publicationDate")).thenReturn("2000-12-12");
        when(mockedRequest.getParameter("price")).thenReturn("10");
        when(mockedRequest.getParameter("currency")).thenReturn("uan");
        when(mockedRequest.getParameter("count")).thenReturn("7");
        when(mockedBookService.findByTitleAndAuthorsNames(titleUa, authorNamesUa, authorNamesEN))
                .thenReturn(Optional.of(mockedBook));

        String expected = "/user/admin/bookForm.jsp?createError=true";
        String actual = addBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void executeWithCreateBookFailure() {
        String titleUa = "Заголовок";
        List<String> authorNamesUa = Collections.singletonList("Автор");
        List<String> authorNamesEN = Collections.singletonList("Author");
        when(mockedRequest.getParameter("titleUa")).thenReturn(titleUa);
        when(mockedRequest.getParameter("authorsUa")).thenReturn("Автор");
        when(mockedRequest.getParameter("descriptionUa")).thenReturn("Опис");
        when(mockedRequest.getParameter("bookLanguageUa")).thenReturn("Українська");
        when(mockedRequest.getParameter("editionUa")).thenReturn("Видання");
        when(mockedRequest.getParameter("titleEn")).thenReturn("Title");
        when(mockedRequest.getParameter("authorsEn")).thenReturn("Author");
        when(mockedRequest.getParameter("descriptionEn")).thenReturn("Description");
        when(mockedRequest.getParameter("bookLanguageEn")).thenReturn("Ukrainian");
        when(mockedRequest.getParameter("editionEn")).thenReturn("Edition");
        when(mockedRequest.getParameter("publicationDate")).thenReturn("2000-12-12");
        when(mockedRequest.getParameter("price")).thenReturn("10");
        when(mockedRequest.getParameter("currency")).thenReturn("uan");
        when(mockedRequest.getParameter("count")).thenReturn("7");
        when(mockedBookService.findByTitleAndAuthorsNames(titleUa, authorNamesUa, authorNamesEN))
                .thenReturn(Optional.empty());
        when(mockedBookService.createBook(any(Book.class))).thenReturn(false);

        String expected = "/error/error.jsp";
        String actual = addBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }

    @Test
    public void execute() {
        String titleUa = "Заголовок";
        List<String> authorNamesUa = Collections.singletonList("Автор");
        List<String> authorNamesEN = Collections.singletonList("Author");
        when(mockedRequest.getParameter("titleUa")).thenReturn(titleUa);
        when(mockedRequest.getParameter("authorsUa")).thenReturn("Автор");
        when(mockedRequest.getParameter("descriptionUa")).thenReturn("Опис");
        when(mockedRequest.getParameter("bookLanguageUa")).thenReturn("Українська");
        when(mockedRequest.getParameter("editionUa")).thenReturn("Видання");
        when(mockedRequest.getParameter("titleEn")).thenReturn("Title");
        when(mockedRequest.getParameter("authorsEn")).thenReturn("Author");
        when(mockedRequest.getParameter("descriptionEn")).thenReturn("Description");
        when(mockedRequest.getParameter("bookLanguageEn")).thenReturn("Ukrainian");
        when(mockedRequest.getParameter("editionEn")).thenReturn("Edition");
        when(mockedRequest.getParameter("publicationDate")).thenReturn("2000-12-12");
        when(mockedRequest.getParameter("price")).thenReturn("10");
        when(mockedRequest.getParameter("currency")).thenReturn("uan");
        when(mockedRequest.getParameter("count")).thenReturn("7");
        when(mockedBookService.findByTitleAndAuthorsNames(titleUa, authorNamesUa, authorNamesEN))
                .thenReturn(Optional.empty());
        when(mockedBookService.createBook(any(Book.class))).thenReturn(true);

        String expected = "/user/admin/bookForm.jsp?successCreation=true";
        String actual = addBook.execute(mockedRequest);

        assertEquals(expected, actual);
    }
}
