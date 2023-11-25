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
    <title>No access page</title>
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
        <c:if test="${sessionScope.userLogin == null}">
            <a class="btn btn-outline-dark mr-2" data-mdb-ripple-color="dark"
               href="${pageContext.request.contextPath}/app/signup">
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
    <div class="container text-center" style="margin-top: 10%;">
        <h3>Немає доступу</h3>
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
        </div>    </div>
</body>
<footer class="navbar fixed-bottom d-flex flex-row justify-content-sm-between align-items-center bg-light text-lg-start p-3">
    <div>
        <p>
            <fmt:message key="footer.licence"/>
            <a href="https://github.com/Koroliuk/Library_servlet/blob/main/LICENSE">
                GNU GPLv3 License
            </a>.
            <br>
            <a href="https://github.com/Koroliuk/Library_servlet"><fmt:message key="footer.project.github"/></a><br/>
            <span>@2021</span>
        </p>
    </div>
    <div>
        <p>
            <fmt:message key="footer.questions"/>
            <a href="https://github.com/Koroliuk/Library_servlet/issues/new">GitHub</a>.
        </p>
    </div>
</footer>
</html>