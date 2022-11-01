<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:if test="${requestScope.amountOfPages > 1}">
    <div style="position: relative">
        <ul class="pagination">
            <c:if test="${requestScope.currentPage > 1}">
                <li class="page-item">
                    <a class="page-link" href="/switch_page?page=${requestScope.currentPage - 1}"><fmt:message key="shop.pagination.previous"/></a>
                </li>
            </c:if>
            <c:forEach begin="1" end="${requestScope.amountOfPages}" var="i">
                <li class="page-item">
                    <a class="page-link" href="/switch_page?page=${i}">${i}</a>
                </li>
            </c:forEach>
            <c:if test="${requestScope.currentPage < requestScope.amountOfPages}">
                <li class="page-item">
                    <a class="page-link" href="/switch_page?page=${requestScope.currentPage + 1}"><fmt:message key="shop.pagination.next"/></a>
                </li>
            </c:if>
        </ul>
    </div>
</c:if>