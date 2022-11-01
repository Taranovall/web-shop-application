<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="captchaCookies" value="${requestScope.CookieWithCaptchaID.value}"/>

<div>
    <img src="/captcha/Captcha_${pageContext.session.id}.jpg" class="img-thumbnail">
    <c:if test="${captchaCookies != null}">
        <input hidden value="${captchaCookies}">
    </c:if>
    <div class="input-group">
        <input type="text" class="form-control" placeholder="Captcha" name="captcha" id="captcha">
    </div>
</div>
