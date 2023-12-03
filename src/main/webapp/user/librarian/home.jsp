<%@ page import="ua.training.model.entity.Book" %>
<%@ page import="ua.training.model.entity.Author" %>
<%@ page import="ua.training.model.entity.Order" %>
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
    <title>Librarian cabinet</title>
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
    <nav class="nav nav-tabs nav-justified">
        <a class="nav-item nav-link <c:if test="${param.tab == null || param.tab.equals('') || param.tab.equals('1')}">active</c:if>"
           data-toggle="tab" href="#orders">Замовлення</a>
        <a class="nav-item nav-link <c:if test="${param.tab.equals('2')}">active</c:if>" data-toggle="tab" href="#subscriptions">Абонементи користувачів</a>
    </nav>
    <br/>
    <div class="tab-content text-center">
        <h3>Кабінет бібліотекаря: <%= session.getAttribute("userLogin") %></h3>
        <div class="tab-pane fade <c:if test="${param.tab == null || param.tab.equals('') || param.tab.equals('1')}">show active</c:if> text-center" id="orders">
            <div class="row justify-content-center">
                <div class="col-auto">
                    <h5 class="mb-2 mt-2">Перелік замовлень</h5>
                    <c:if test="${requestScope.orderList != null}">
                        <table class="table table-responsive table-bordered table-hover">
                            <thead class="thead-light">
                                <tr>
                                    <th>№</th>
                                    <th>User login</th>
                                    <th>Title</th>
                                    <th>Authors</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <c:if test="${requestScope.orderList.size() > 0}">
                                <c:forEach items="${requestScope.orderList}" var="order">
                                    <tr>
                                        <td><c:out value="${order.id}"/></td>
                                        <td><c:out value="${order.user.login}"/></td>
                                        <td>
                                            <c:out value="${order.book.title}"/>
                                        </td>
                                        <td>
                                            <%
                                                Order order = (Order) pageContext.getAttribute("order");;
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
                                        <td>
                                            <a  class="btn btn-outline-info" href="${pageContext.request.contextPath}/app/librarian/home?id=${order.id}&action=add">Видати</a>
                                            <a  class="btn btn-outline-danger" href="${pageContext.request.contextPath}/app/librarian/home?id=${order.id}&action=delete">Скасувати</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="tab-pane fade <c:if test="${param.tab.equals('3')}">show active</c:if> text-center" id="subscriptions">
            <div class="row justify-content-center">
                <div class="col-auto">
                    <h5>Абонементи читачів</h5>
                    <c:if test="${requestScope.userList != null}">
                        <table class="table table-responsive table-bordered table-hover">
                            <thead class="thead-light">
                                <tr>
                                    <th>№</th>
                                    <th>Login</th>
                                    <th>Абонемент</th>
                                </tr>
                            </thead>
                            <c:if test="${requestScope.userList.size() > 0}">
                                <c:forEach items="${requestScope.userList}" var="user">
                                    <c:if test="${user.role == 'READER'}">
                                        <tr>
                                            <td>
                                                    ${user.id}
                                            </td>
                                            <td>
                                                    ${user.login}
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/app/librarian/getReaderBooks?userId=${user.id}">Переглянути абонемент</a>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/user/librarian/js/home.js"></script>
</html>
