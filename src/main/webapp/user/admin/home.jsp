<%@ page import="ua.training.model.entity.Author" %>
<%@ page import="ua.training.model.entity.Book" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : sessionScope.language}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html lang="${language}">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Admin cabinet</title>
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
<header class="d-flex align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom bg-light">
    <span class="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none h3"><fmt:message
            key="header.library.name"/></span>
    <div class="d-flex flex-row mr-3">
        <form id="langForm" class="mr-2">
            <select class="custom-select" id="language" name="language" onchange="submit()">
                <option value="en" ${language == 'en' ? 'selected' : ''}><fmt:message
                        key="header.language.english"/></option>
                <option value="uk" ${language == 'uk' ? 'selected' : ''}><fmt:message
                        key="header.language.ukrainian"/></option>
            </select>
        </form>
        <a class="btn btn-dark" href="${pageContext.request.contextPath}/app/logout"><fmt:message key="header.logout"/></a>
    </div>
</header>
<body>
<div class="container text-center">
    <nav class="nav nav-tabs nav-justified">
        <a class="nav-item nav-link <c:if test="${param.tab == null || param.tab.equals('') || param.tab.equals('1')}">active</c:if>"
           data-toggle="tab" href="#users">Користувачі</a>
        <a class="nav-item nav-link <c:if test="${param.tab.equals('2')}">active</c:if>" data-toggle="tab" href="#books">Книги</a>
    </nav>
    <br/>
    <h3>Кабінет користувача: <%= session.getAttribute("userLogin") %></h3>
    <div class="tab-content text-center">
        <div class="tab-pane fade <c:if test="${param.tab == null || param.tab.equals('') || param.tab.equals('1')}">show active</c:if> text-center"
             id="users">
            <div>
                <span class="h5">Користувачі: </span>
                <a class="btn btn-outline-primary btn-sm mb-2 mt-2" href="${pageContext.request.contextPath}/app/admin/addLibrarian">Add
                    Librarian</a>
            </div>
            <div class="row justify-content-center">
                <div class="col-auto">
                    <c:if test="${requestScope.userList != null}">
                        <table class="table table-responsive table-bordered table-hover">
                            <thead class="thead-light">
                                <tr>
                                    <th>№</th>
                                    <th>Login</th>
                                    <th>Role</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <c:if test="${requestScope.userList.size() > 0}">
                                <c:forEach items="${requestScope.userList}" var="user">
                                    <tr>
                                        <td><c:out value="${user.id}"/></td>
                                        <td><c:out value="${user.login}"/></td>
                                        <td><c:out value="${user.role}"/></td>
                                        <td>
                                            <c:if test="${user.blocked == true}">

                                                <a type="button" class="btn btn-outline-success" href="${pageContext.request.contextPath}/app/admin/unblockUser?id=${user.id}">Розблокувати</a>
                                            </c:if>
                                            <c:if test="${user.blocked == false}">
                                                <a type="button" class="btn btn-outline-danger" href="${pageContext.request.contextPath}/app/admin/blockUser?id=${user.id}">Заблокувати</a>
                                            </c:if>
                                            <c:if test="${user.role == 'LIBRARIAN'}">
                                                <a type="button" class="btn btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/app/admin/deleteLibrarian?id=${user.id}">Видалити</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="tab-pane fade <c:if test="${param.tab.equals('2')}">show active</c:if> text-center" id="books">
            <div>
                <span class="h5">Книги: </span><a class="btn btn-outline-primary btn-sm mt-2 mb-2" href="${pageContext.request.contextPath}/app/admin/addBook">Add
                book</a>
            </div>
            <div class="row justify-content-center">
                <div class="col-auto">
                    <c:if test="${requestScope.bookList != null}">
                        <table class="table table-responsive table-bordered table-hover">
                            <thead class="thead-light">
                                <tr>
                                    <th>№</th>
                                    <th>Title</th>
                                    <th>Authors</th>
                                    <th>Language</th>
                                    <th>Edition</th>
                                    <th>Date of publish</th>
                                    <th>Description</th>
                                    <th>Price</th>
                                    <th>Amount</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <c:if test="${requestScope.bookList.size() > 0}">
                                <c:forEach items="${requestScope.bookList}" var="book">
                                    <tr>
                                        <td>${book.id}</td>
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
                                        <td>
                                            <c:if test="${language == 'en'}"><span>${book.price}$</span></c:if>
                                            <c:if test="${language == 'uk'}"><span>${book.price}₴</span></c:if>
                                        </td>
                                        <td>${book.count}</td>
                                        <td class="justify-content-sm-between" style="width: 200px;">
                                            <a type="button" class="btn btn-outline-warning" href="${pageContext.request.contextPath}/app/admin/editBook?id=${book.id}">Edit</a>
                                            <a type="button" class="btn btn-outline-danger" href="${pageContext.request.contextPath}/app/admin/deleteBook?id=${book.id}">Delete</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/user/admin/js/home.js"></script>
</html>
