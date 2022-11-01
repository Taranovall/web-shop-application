<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${sessionScope.user == null}">
        <form action="/sign_in" method="post">
            <div class="modal-header">
                <h5 class="modal-title"><fmt:message key="authorization"/></h5>
            </div>
            <div class="modal-body text-center" style="padding-bottom: 0">
                <p field="${fieldToBeHighlighted}" id="show-error" class="error-message">${error}</p>
                <input data-validation="required username" type="text" name="login"
                       placeholder="<fmt:message key="authorization.login"/>" class="form-control mb-2">
                <input data-validation="required password" type="password" name="password"
                       placeholder="<fmt:message key="authorization.password"/>"
                       class="form-control mb-2">
                <button type="submit" class="btn btn-outline-dark mb-2"><fmt:message key="authorization.sign_in"/>
                </button>
            </div>
            <div class="modal-footer justify-content-center">
                <a class="btn" href="/sign_up"><u><fmt:message key="authorization.have_no_account"/></u></a>
            </div>
        </form>
    </c:when>
    <c:otherwise>
        <form action="/sign_out" method="post">
            <div class="modal-header">
                <h5 class="modal-title"><fmt:message key="authorization.you_are_signed_in"/></h5>
            </div>
            <div class="modal-body text-center" style="padding-bottom: 0">
                <p>${sessionScope.user.login}</p>
                <img class="image" src="${sessionScope.user.profilePicturePath}"/>
                <button type="submit" class="btn btn-outline-dark mb-2"><fmt:message
                        key="authorization.sign_out"/></button>
            </div>
        </form>
    </c:otherwise>
</c:choose>

