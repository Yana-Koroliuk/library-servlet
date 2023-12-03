<%@ page import="ua.training.model.entity.Order" %>
<%@ page import="ua.training.model.entity.Book" %>
<%@ page import="ua.training.model.entity.Author" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
    <title>Reader subscription  page</title>
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
                <option value="${pageContext.request.contextPath}/app/librarian/getReaderBooks?userId=${param.userId}&language=en"
                ${language == 'en' ? 'selected' : ''}>
                    <fmt:message key="header.language.english"/></option>
                <option value="${pageContext.request.contextPath}/app/librarian/getReaderBooks?userId=${param.userId}&language=ua"
                ${language == 'uk' ? 'selected' : ''}>
                    <fmt:message key="header.language.ukrainian"/></option>
            </select>
        </form>
        <a class="btn btn-dark" href="${pageContext.request.contextPath}/app/logout"><fmt:message key="header.logout"/></a>
    </div>
</header>
<body>
<div class="container text-center">
    <h3 class="mb-2">Абонемент користувача: ${requestScope.user.login}</h3>
    <div class="row justify-content-center">
        <div class="col-auto">
            <c:if test="${requestScope.orderList != null}">
                <table class="table table-responsive table-bordered table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>Id</th>
                            <th>User login</th>
                            <th>Title</th>
                            <th>Authors</th>
                            <th>Edition</th>
                            <th>Publication date</th>
                            <th>Language</th>
                        </tr>
                    </thead>
                    <c:forEach items="${requestScope.orderList}" var="order">
                        <tr>
                            <td><c:out value="${order.id}"/></td>
                            <td><c:out value="${order.user.login}"/></td>
                            <td>
                                <c:out value="${order.book.title}"/>
                            </td>
                            <td>
                                <%
                                    Order order = (Order) pageContext.getAttribute("order");
                                    ;
                                    Book book = order.getBook();
                                    String authors = "";
                                    StringBuilder authorsString = new StringBuilder();
                                    for (Author author : book.getAuthors()) {
                                        authorsString.append(author.getName()).append(",").append(" ");
                                    }
                                    authorsString.deleteCharAt(authorsString.length() - 1);
                                    authorsString.deleteCharAt(authorsString.length() - 1);
                                    authors = authorsString.toString();
                                    request.setAttribute("authorsString", authors);
                                %>
                                <span>${requestScope.authorsString}</span>
                            </td>
                            <td>${order.book.edition.name}</td>
                            <td>${order.book.publicationDate}</td>
                            <td>${order.book.language}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
    </div>
    <div>
        <a href="${pageContext.request.contextPath}/app/librarian/home"><fmt:message key="global.to.home.page"/></a>
    </div>
</div>
</body>
</html>
