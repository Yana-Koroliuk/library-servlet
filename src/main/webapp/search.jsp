<%@ page import="ua.training.model.entity.Book" %>
<%@ page import="ua.training.model.entity.Author" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : sessionScope.language}"
       scope="session"/>
<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="${language}">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search Page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
</head>
<header class="d-flex align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
    <span class="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none h3"><fmt:message
            key="header.library.name"/></span>
    <div class="d-flex flex-row mr-3">
        <form class="mr-2">
            <select class="custom-select" id="language" name="language" onChange="window.document.location.href=this.options[this.selectedIndex].value;">
                <option value="${pageContext.request.contextPath}/app/search?keyWords=${param.keyWords}&page=${param.page}&sortBy=${param.sortBy}&sortType=${param.sortType}&language=en"
                ${language == 'en' ? 'selected' : ''}>
                    <fmt:message key="header.language.english"/></option>
                <option value="${pageContext.request.contextPath}/app/search?keyWords=${param.keyWords}&page=${param.page}&sortBy=${param.sortBy}&sortType=${param.sortType}&language=ua"
                ${language == 'uk' ? 'selected' : ''}>
                    <fmt:message key="header.language.ukrainian"/></option>
            </select>
        </form>
        <c:if test="${sessionScope.userLogin == null}">
            <a class="btn btn-outline-dark mr-2" data-mdb-ripple-color="dark" href="${pageContext.request.contextPath}/app/signup">
                <fmt:message key="header.signup"/>
            </a>
            <a class="btn btn-dark" href="${pageContext.request.contextPath}/app/login">
                <fmt:message key="header.login"/></a>
        </c:if>
        <c:if test="${sessionScope.userLogin != null}">
            <a class="btn btn-dark" href="${pageContext.request.contextPath}/app/logout">
                <fmt:message key="header.logout"/></a>
        </c:if>
    </div>
</header>
<body>
    <div class="container text-center">
        <h3>Catalog</h3>
        <div>
            <form method="post" action="${pageContext.request.contextPath}/app/search?page=1">
                <label>
                    <input class="form-control me-2 mr-2" type="text" name="keyWords" placeholder="Пошук">
                </label>
                <input class="btn btn-outline-success p-1" type="submit" value="Знайти">
            </form>
        </div>
        <c:if test="${sessionScope.userLogin == null}">
            <a href="${pageContext.request.contextPath}/app/login" class="text-info mt-2 mb-2">Увійдйть для замовлення книги</a>
        </c:if>
        <span class="text-danger mb-2 mt-2" id="warning" hidden>Немає доступних екземплярів, спробуйте пізніше</span>
        <div class="row justify-content-center">
            <div class="col-auto">
                <c:if test="${requestScope.bookList != null}">
                    <table class="table table-responsive table-bordered table-hover">
                        <thead class="thead-light">
                            <tr>
                                <th>Title</th>
                                <th>Authors</th>
                                <th>Language</th>
                                <th>Edition</th>
                                <th>Date of publish</th>
                                <th>Description</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                        <div class="mr-3 mt-2 mb-2">
                            <span>Сортувати за: </span>
                            <%
                                String sortBy = request.getParameter("sortBy");
                                String currSortType = request.getParameter("sortType");
                                HashMap<String, String> sortTypes = new HashMap<String, String>();
                                if (currSortType.equals("asc")) {
                                    sortTypes.put(sortBy, "dec");
                                } else if (currSortType.equals("dec")) {
                                    sortTypes.put(sortBy, "asc");
                                } else {
                                    sortTypes.put(sortBy, "asc");
                                }
                                request.setAttribute("sortTypes", sortTypes);
                            %>
                            <a type="button" class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/app/search?keyWords=${param.keyWords}&page=${param.page.trim()}&sortBy=title&sortType=<c:if test="${requestScope.sortTypes.containsKey('title')}">${requestScope.sortTypes.get('title')}</c:if><c:if test="${!requestScope.sortTypes.containsKey('title')}">asc</c:if>">назвою</a>
                            <a type="button" class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/app/search?keyWords=${param.keyWords}&page=${param.page.trim()}&sortBy=author&sortType=<c:if test="${requestScope.sortTypes.containsKey('author')}">${requestScope.sortTypes.get('author')}</c:if><c:if test="${!requestScope.sortTypes.containsKey('author')}">asc</c:if>">автором</a>
                            <a type="button" class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/app/search?keyWords=${param.keyWords}&page=${param.page.trim()}&sortBy=edition&sortType=<c:if test="${requestScope.sortTypes.containsKey('edition')}">${requestScope.sortTypes.get('edition')}</c:if><c:if test="${!requestScope.sortTypes.containsKey('edition')}">asc</c:if>">виданням</a>
                            <a type="button" class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/app/search?keyWords=${param.keyWords}&page=${param.page.trim()}&sortBy=date&sortType=<c:if test="${requestScope.sortTypes.containsKey('date')}">${requestScope.sortTypes.get('date')}</c:if><c:if test="${!requestScope.sortTypes.containsKey('date')}">asc</c:if>">датою видання</a>
                        </div>
                        <c:forEach items="${requestScope.bookList}" var="book">
                            <tr>
                                <td><c:out value="${book.title}"/></td>
                                <td>
                                    <%
                                        Book book = (Book) pageContext.getAttribute("book");
                                        StringBuilder authorsString = new StringBuilder();
                                        for (Author author : book.getAuthors()) {
                                            authorsString.append(author.getName()).append(",").append(" ");
                                        }
                                        authorsString.deleteCharAt(authorsString.length() - 1);
                                        authorsString.deleteCharAt(authorsString.length() - 1);
                                        out.print(authorsString.toString());
                                    %>
                                </td>
                                <td>${book.language}</td>
                                <td>${book.edition.name}</td>
                                <td>${book.publicationDate}</td>
                                <td>${book.description}</td>
                                <c:if test="${sessionScope.role == 'READER'}">
                                    <td>
                                        <a id="btn${book.id}" type="button" class="btn btn-outline-info" onclick="checkAmountOfBook(${book.count}, ${book.id})" href="${pageContext.request.contextPath}/app/reader/orderBook?bookId=${book.id}&userLogin=${sessionScope.userLogin}">Замовити</a>
                                    </td>
                                </c:if>
                                <c:if test="${sessionScope.role != 'READER'}">
                                    <td>
                                        <span>-</span>
                                    </td>
                                </c:if>
                            </tr>
                            <tr id="additionalInfo${book.id}" hidden>
                                <td>
                                    Language: <c:out value="${book.language}"/><br/>
                                    Edition: <c:out value="${book.edition.name}"/><br/>
                                    Date of publish: <br/> <c:out value="${book.publicationDate}"/>
                                </td>
                                <td>
                                    Description:<br/><c:out value="${book.description}"/><br/>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>
                <c:if test="${requestScope.bookList.size() == 0}">
                    <p>Пусто</p>
                </c:if>
            </div>
        </div>
        <div class="btn-group" role="group" aria-label="Basic example">
            <c:forEach begin="1" end="${param.amountOfPages}" var="numberOfPage">
                <a class="btn btn-primary btn-rounded" href="${pageContext.request.contextPath}/app/search?keyWords=${param.keyWords}&sortBy=${param.sortBy}&sortType=${param.sortType}&page=${numberOfPage}">${numberOfPage}</a>
            </c:forEach>
        </div>
        <br/>
        <br/>
        <div>
            <c:if test="${sessionScope.userLogin == null}">
                <a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="global.to.home.page"/></a>
            </c:if>
            <c:if test="${sessionScope.userLogin != null}">
                <c:if test="${sessionScope.role == 'READER'}">
                    <a href="${pageContext.request.contextPath}/app/reader/home"><fmt:message key="global.to.home.page"/></a>
                </c:if>
                <c:if test="${sessionScope.role == 'LIBRARIAN'}">
                    <a href="${pageContext.request.contextPath}/app/librarian/home"><fmt:message key="global.to.home.page"/></a>
                </c:if>
                <c:if test="${sessionScope.role == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/app/admin/home"><fmt:message key="global.to.home.page"/></a>
                </c:if>
            </c:if>
        </div>
    </div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/public/js/search.js"></script>
<script type="text/javascript">
    function checkAmountOfBook(bookAmount, bookId) {
        const button = document.getElementById('btn'+bookId);
        const warning = document.getElementById('warning');
        if (bookAmount <= 0) {
            warning.hidden = false;
            button.setAttribute("href", "#");
        } else {
            warning.hidden = true;
            button.setAttribute("href", "${pageContext.request.contextPath}/app/reader/orderBook?bookId="+bookId+"&userLogin=${sessionScope.userLogin}")
        }
    }
</script>
</html>
