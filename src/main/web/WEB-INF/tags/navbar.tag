<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${pageContext.request.locale.language}" scope="session"/>
<fmt:setBundle basename="locale" scope="session"/>

<nav class="navbar navbar-expand navbar-light bg-light" style="border: 1px solid rgba(0,0,0,.125);">
    <a href="/" class="navbar-brand mb-0 h1" style="position: absolute;left: 1%;">Web Shop</a>
    <div class="collapse navbar-collapse" id="navbar-content">
        <ul class="navbar-nav mx-auto navbar-account">
            <li class="nav-item">
                <a class="nav-button btn btn-outline-dark" href="/sign_in"><fmt:message key="authorization.sign_in"/></a>
            </li>
            <li class="nav-item" style="position: absolute; left: 85%;">
                <t:changeLanguageMenu/>
            </li>
            <t:cart/>
        </ul>
    </div>
</nav>