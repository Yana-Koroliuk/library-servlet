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
    <title>Registration page for librarian</title>
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
<div class="container text-center" style="margin-top: 10%;">
    <h3>Створення бібліотекаря</h3>
    <form id="form" method="post" action="${pageContext.request.contextPath}/app/admin/addLibrarian">
        <div class="form-group">
            <c:if test="${param.validError == true}">
                <span class="text-danger"><fmt:message key="signup.message.check.input.data"/></span>
                <br/>
            </c:if>
            <c:if test="${param.loginError == true}">
                <span class="text-danger"><fmt:message key="signup.message.login.in.use"/></span>
                <br/>
            </c:if>
            <c:if test="${param.successEvent == true}">
                <span class="text-success"><fmt:message key="signup.success.registration"/></span>
                <br/>
            </c:if>
            <div>
                <label style="width: 230px;">
                    <input class="form-control" type="text" id="login" name="login"
                           placeholder="<fmt:message key="global.login"/>">
                    <span class="text-danger" style="text-align: justify;" id="loginMessage"></span>
                </label>
            </div>
            <div>
                <label style="width: 230px;">
                    <input class="form-control" type="password" id="password" name="password"
                           placeholder="<fmt:message key="global.password"/>">
                    <span class="text-danger" style="text-align: justify;" id="passwordMessage"></span>
                </label>
            </div>
            <div>
                <input type="submit" class="btn btn-outline-info" value="<fmt:message key="signup.button.name"/>">
            </div>
        </div>
    </form>
    <div>
        <a style="padding-left: 130px;" href="${pageContext.request.contextPath}/app/admin/home"><fmt:message key="global.to.home.page"/></a>
    </div>
</div>
</body>
<footer class="navbar fixed-bottom d-flex flex-row justify-content-sm-between align-items-center bg-light text-lg-start p-3">
    <div>
        <p>
            <fmt:message key="footer.licence"/>
            <a href="https://github.com/Yana-Koroliuk/library-servlet/blob/main/LICENSE">
                GNU GPLv3 License
            </a>.
            <br>
            <a href="https://github.com/Yana-Koroliuk/library-servlet"><fmt:message key="footer.project.github"/></a><br/>
            <span>@2023</span>
        </p>
    </div>
    <div>
        <p>
            <fmt:message key="footer.questions"/>
            <a href="https://github.com/Yana-Koroliuk/library-servlet/issues/new">GitHub</a>.
        </p>
    </div>
</footer>
<script type="text/javascript">
    const loginValidateMessage1 = '<fmt:message key="signup.login.validation.message1"/>';
    const loginValidateMessage2 = '<fmt:message key="signup.login.validation.message2"/>';
    const passwordValidateMessage1 = '<fmt:message key="signup.password.validation.message1"/>';
    const passwordValidateMessage2 = '<fmt:message key="signup.password.validation.message2"/>';
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/user/admin/js/librarianForm.js"></script>
</html>

