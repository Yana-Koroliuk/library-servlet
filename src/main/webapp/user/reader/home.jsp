<%@ page import="ua.training.model.entity.Book" %>
<%@ page import="ua.training.model.entity.Author" %>
<%@ page import="ua.training.model.entity.Order" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.Period" %>
<%@ page import="java.math.BigDecimal" %>
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
    <title>User home page</title>
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
        <a class="nav-item nav-link <c:if test="${param.tab == null || param.tab.equals('') || param.tab.equals('1')}">active</c:if>" data-toggle="tab" href="#subscription">Абонемент</a>
        <a class="nav-item nav-link <c:if test="${param.tab.equals('2')}">active</c:if>" data-toggle="tab" href="#readingHole">Читальний зал</a>
        <a class="nav-item nav-link <c:if test="${param.tab.equals('3')}">active</c:if>" data-toggle="tab" href="#orders">Замовлення</a>
    </nav>
    <br/>
    <div class="tab-content text-center">
        <h4>Кабінет користувача: <%= session.getAttribute("userLogin") %></h4>
        <div>
            <form method="post" action="${pageContext.request.contextPath}/app/search?page=1">
                <label>
                    <input class="form-control me-2 mr-2" type="text" name="keyWords" placeholder="Пошук">
                </label>
                <input class="btn btn-outline-success p-1" type="submit" value="Знайти">
            </form>
        </div>
        <c:if test="${param.successOrder == true}">
            <span class="text-success">Книгу замовлено успішно</span>
        </c:if>
        <br/>
        <div class="tab-pane fade <c:if test="${param.tab == null || param.tab.equals('') || param.tab.equals('1')}">show active</c:if> text-center" id="subscription">
            <h5>Абонемент</h5>
            <div class="row justify-content-center">
                <div class="col-auto">
                    <table class="table table-responsive table-bordered table-hover">
                        <thead class="thead-light">
                            <tr>
                                <th>№</th>
                                <th>Title</th>
                                <th>Authors</th>
                                <th>Edition</th>
                                <th>Language</th>
                                <th>Start date</th>
                                <th>End date</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <c:if test="${requestScope.orderList.size() > 0}">
                            <c:forEach items="${requestScope.orderList}" var="order">
                                <c:if test="${order.orderStatus == 'APPROVED' || order.orderStatus == 'OVERDUE'}">
                                    <tr>
                                        <td>${order.id}</td>
                                        <td>${order.book.title}</td>
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
                                                ${requestScope.authorsString}
                                        </td>
                                        <td>${order.book.edition.name}</td>
                                        <td>${order.book.language}</td>
                                        <td>${order.startDate}</td>
                                        <td>${order.endDate}</td>
                                        <c:if test="${order.orderStatus == 'APPROVED'}">
                                            <td>
                                                <a type="button" class="btn btn-outline-info"
                                                   href="${pageContext.request.contextPath}/app/reader/home?orderId=${order.id}&tab=1">Повернути
                                                    книгу</a>
                                            </td>
                                        </c:if>
                                        <c:if test="${order.orderStatus == 'OVERDUE'}">
                                            <%
                                                LocalDate now = LocalDate.now();
                                                LocalDate end = order.getEndDate();
                                                int amountOfDays = Period.between(end, now).getDays();
                                                BigDecimal fine = book.getPrice().multiply(new BigDecimal(amountOfDays)).multiply(BigDecimal.valueOf(0.01));
                                                request.setAttribute("fine", fine);
                                            %>
                                            <td>
                                                <a type="button" class="btn btn-outline-warning"
                                                   href="${pageContext.request.contextPath}/app/reader/home?orderId=${order.id}&tab=1">Pay
                                                    fine ${fine}<br/>and return book</a>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
        <div class="tab-pane fade <c:if test="${param.tab.equals('2')}">show active</c:if> text-center" id="readingHole">
            <h3>Читальний зал</h3>
            <div class="row justify-content-center">
                <div class="col-auto">
                    <table class="table table-responsive table-bordered table-hover">
                        <thead class="thead-light">
                            <tr>
                                <th>№</th>
                                <th>Title</th>
                                <th>Authors</th>
                                <th>Edition</th>
                                <th>Language</th>
                                <th>Date</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <c:if test="${requestScope.orderList.size() > 0}">
                            <c:forEach items="${requestScope.orderList}" var="order">
                                <c:if test="${order.orderStatus == 'READER_HOLE'}">
                                    <tr>
                                        <td>${order.id}</td>
                                        <td>${order.book.title}</td>
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
                                                ${requestScope.authorsString}
                                        </td>
                                        <td>${order.book.edition.name}</td>
                                        <td>${order.book.language}</td>
                                        <td>${order.endDate}</td>
                                        <td>
                                            <a type="button" class="btn btn-outline-danger"
                                               href="${pageContext.request.contextPath}/app/reader/home?orderId=${order.id}&tab=2">Скасувати замовлення</a>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
            </div>
        </div>
        <div class="tab-pane fade <c:if test="${param.tab.equals('3')}">show active</c:if> text-center" id="orders">
            <h3>Замовлення</h3>
            <div class="row justify-content-center">
                <div class="col-auto">
                    <table class="table table-responsive table-bordered table-hover">
                        <thead class="thead-light">
                            <tr>
                                <th>№</th>
                                <th>Title</th>
                                <th>Authors</th>
                                <th>Edition</th>
                                <th>Language</th>
                                <th>Start date</th>
                                <th>End date</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <c:if test="${requestScope.orderList.size() > 0}">
                            <c:forEach items="${requestScope.orderList}" var="order">
                                <c:if test="${order.orderStatus == 'CANCELED' || order.orderStatus == 'RECEIVED'}">
                                    <tr>
                                        <td>${order.id}</td>
                                        <td>${order.book.title}</td>
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
                                                ${requestScope.authorsString}
                                        </td>
                                        <td>${order.book.edition.name}</td>
                                        <td>${order.book.language}</td>
                                        <td>${order.startDate}</td>
                                        <td>${order.endDate}</td>
                                        <td>${order.orderStatus}</td>
                                        <c:if test="${order.orderStatus == 'RECEIVED'}">
                                            <td>
                                                <a type="button" class="btn btn-outline-danger"
                                                   href="${pageContext.request.contextPath}/app/reader/home?orderId=${order.id}&tab=3">Скасувати
                                                    <br/> замовлення</a>
                                            </td>
                                        </c:if>
                                        <c:if test="${order.orderStatus == 'CANCELED'}">
                                            <td>
                                                <a type="button" class="btn btn-outline-dark"
                                                   href="${pageContext.request.contextPath}/app/reader/home?orderId=${order.id}&tab=3">Видалити <br/> повідомлення</a>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:if>

                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
