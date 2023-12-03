<%@ page import="ua.training.model.entity.Book" %>
<%@ page import="ua.training.model.entity.Author" %>
<%@ page import="java.time.LocalDate" %>
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
    <title>Order book page</title>
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
                <option value="${pageContext.request.contextPath}/app/reader/orderBook?bookId=${param.bookId}&userLogin=${param.userLogin}&language=en"
                ${language == 'en' ? 'selected' : ''}>
                    <fmt:message key="header.language.english"/></option>
                <option value="${pageContext.request.contextPath}/app/reader/orderBook?bookId=${param.bookId}&userLogin=${param.userLogin}&language=ua"
                ${language == 'uk' ? 'selected' : ''}>
                    <fmt:message key="header.language.ukrainian"/></option>
            </select>
        </form>
        <a class="btn btn-dark" href="${pageContext.request.contextPath}/app/logout"><fmt:message key="header.logout"/></a>
    </div>
</header>
<body>
<div class="container text-center">
    <h3>Замовити книгу</h3>
    <div class="row justify-content-center">
        <div class="col-auto">
            <table class="table table-responsive">
                <tr>
                    <td class="text-left">
                        <span>Заголовок: ${requestScope.book.title}</span>
                    </td>
                </tr>
                <tr>
                    <td class="text-left">Автори:
                        <%
                            Book book = (Book) request.getAttribute("book");
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
                </tr>
                <tr>
                    <td class="text-left">
                        <span>Короткий опис:<br/> ${requestScope.book.description}</>
                    </td>
                </tr>
                <tr>
                    <td class="text-left">
                        <span>Мова: ${requestScope.book.language}</span>
                    </td>
                </tr>
                <tr>
                    <td class="text-left">
                        <span>Видання: ${requestScope.book.edition.name}</span>
                    </td>
                </tr>
                <tr>
                    <td class="text-left">
                        <span>Дата видання: ${requestScope.book.publicationDate}</span>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <form id="orderForm" method="post" action="${pageContext.request.contextPath}/app/reader/orderBook?bookId=${requestScope.book.id}&userLogin=${requestScope.user.login}">
        <div class="form-group">
            <div>
                <label for="orderType">Тип замовлення:</label><br/>
                <select class="custom-select-sm" style="width: 200px;" id="orderType" name="orderType" onchange="chooseType()">
                    <option value="subscription">Абонемент</option>
                    <option value="readingHole">Читальний зал</option>
                </select>
            </div>
            <div style="padding-left: 10px;">
                <c:if test="${param.validError == true}">
                    <span class="text-danger" style="text-align: justify;">Перевірте правильність вибору дат</span>
                    <br/>
                </c:if>
                <c:if test="${param.amountError == true}">
                    <span class="text-info" style="text-align: justify;">Немає екземплярів спробуйте пізніше</span>
                    <br/>
                </c:if>
                <span class="text-danger" style="text-align: justify;" id="warning3" hidden>Дата початку має буте не пізніше дати кінця</span>
                <span class="text-danger" style="text-align: justify;" id="warning1" hidden>Оберіть дату не раніше сьогодні</span>
                <span class="text-danger" style="text-align: justify;" id="warning2" hidden>Оберіть дату не раніше сьогодні</span>
            </div>
            <div class="mb-2 mt-2">
                <label>Start date: <input class="form-control" id="startDate" type="date" name="startDate" value="<%= LocalDate.now() %>">
                </label>
            </div>
            <div class="mb-2 mt-2">
                <label>End date: <input class="form-control" id="endDate" type="date" name="endDate" value="<%= LocalDate.now() %>">
                </label>
            </div>
            <div>
                <input class="btn btn-outline-info" type="submit" value="Замовити">
            </div>
        </div>
    </form>
    <br/>
    <div style="padding-left: 200px;">
        <a href="${pageContext.request.contextPath}/app/search?page=1&keyWords=">To search</a>
        <br/>
        <a href="${pageContext.request.contextPath}/app/reader/home">To cabinet</a>
    </div>
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/user/reader/js/orderBook.js"></script>
</html>
