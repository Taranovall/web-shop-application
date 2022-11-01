<jsp:useBean id="userBean" scope="request" class="web.shop.entity.User"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Registration</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="/style/auth.css">
    <link rel="stylesheet" type="text/css" href="/style/productPage.css">
    <script defer src="/js/jQueryValidation.js"></script>
</head>
<body>
<t:navbar/>
<div class="container">
    <form id="sign-up-form" action="/sign_up" method="post" class="auth-menu" enctype="multipart/form-data">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><fmt:message key="authorization.registration"/></h5>
                </div>
                <div class="modal-body text-center" style="padding-bottom: 0">
                    <p field="${fieldToBeHighlighted}" id="show-error" class="error-message">${error}</p>
                    <input data-validation="required username" id="login" type="text" name="login" placeholder="<fmt:message key="authorization.login"/>"
                           value="${userBean.login}" class="form-control mb-2">
                    <input data-validation="required username" id="first_name" type="text" name="first_name"
                           placeholder="<fmt:message key="authorization.first_name"/>" value="${userBean.firstName}" class="form-control mb-2">
                    <input data-validation="required username" id="second_name" type="text" name="second_name"
                           placeholder="<fmt:message key="authorization.second_name"/>" value="${userBean.secondName}" class="form-control mb-2">
                    <input data-validation="required password" id="first-password" type="password"
                           name="password" placeholder="<fmt:message key="authorization.password"/>" class="form-control mb-2">
                    <input data-validation="required password" id="second-password" type="password"
                           name="passwordConfirm" placeholder="<fmt:message key="authorization.repeat_password"/>" class="form-control mb-2">
                    <input data-validation="required email" id="email" type="text" name="email" placeholder="<fmt:message key="authorization.email"/>"
                           value="${userBean.mail}" class="form-control mb-2">
                    <input type="file" class="form-control mb-2" name="profilePic" accept="image/*">
                    <div style="text-align: left!important;">
                        <c:forEach items="${newsletters}" var="n">
                            <div class="form-check form-switch mb-2">
                                <c:choose>
                                    <c:when test="${userBean.newsletterList.contains(n)}">
                                        <input class="form-check-input" type="checkbox" id="n" name="newsletters"
                                               value="${n}" checked>
                                    </c:when>
                                    <c:otherwise>
                                        <input class="form-check-input" type="checkbox" id="n" name="newsletters"
                                               value="${n}">
                                    </c:otherwise>
                                </c:choose>
                                <label class="form-check-label" for="n">${n.getMessage()}</label>
                            </div>
                        </c:forEach>
                    </div>
                    <t:captcha/>
                    <button type="submit" class="btn btn-outline-dark mb-2"><fmt:message key="authorization.sign_up"/></button>
                    <div class="modal-footer justify-content-center">
                        <a class="btn" href="/sign_in"><u><fmt:message key="authorization.have_account"/></u></a>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <br>
</div>
</body>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous">
</script>
</html>
