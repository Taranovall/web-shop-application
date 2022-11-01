<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<jsp:useBean id="localeUtil" scope="application" type="web.shop.util.locale.LocaleUtil"/>
<c:set value="${pageContext.request.locale}" var="currentLocale"/>

<form method="post" action="/change-language">
    <select class="form-select language" name="lang" onchange="if(this.value !== 0) { this.form.submit();}">
        <option value="${currentLocale}" selected disabled hidden>${localeUtil.getFlagByLocale(currentLocale)}</option>
        <c:forEach var="l" items="${applicationScope.availableLocales}">
            <option value="${l.getLanguage()}">${localeUtil.getFlagByLocale(l)} ${l.getDisplayLanguage()}</option>
        </c:forEach>
    </select>
</form>
