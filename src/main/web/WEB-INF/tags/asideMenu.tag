<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%--Visible only on XS screen--%>
<div class="d-block d-sm-none xs-option-container collapse-button">
    <a data-toggle="collapse" href="#findProducts">Поиск</a>
</div>
<%--/Visible only on XS screen--%>
<form method="get" class="mt-2" action="/product-filter">
    <%--find--%>
    <div class="card" id="findProducts">
        <div class="card-header"><fmt:message key="aside_menu.search"/></div>
        <div class="card-body">
            <div class="input-group">
                <input type="search" name="searchQuery" class="form-control" value="${requestScope.query}" placeholder="<fmt:message key="aside_menu.search"/>">
                <button class="btn btn-success" type="submit">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                         class="bi bi-search" viewBox="0 0 16 16">
                        <path fill-rule="evenodd"
                              d="M10.442 10.442a1 1 0 0 1 1.415 0l3.85 3.85a1 1 0 0 1-1.414 1.415l-3.85-3.85a1 1 0 0 1 0-1.415z"/>
                        <path fill-rule="evenodd"
                              d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zM13 6.5a6.5 6.5 0 1 1-13 0 6.5 6.5 0 0 1 13 0z"/>
                    </svg>
                </button>
            </div>
            <%--FILTERS--%>
            <div class="collapse-button">
                <a data-toggle="collapse" href="#searchOptions" aria-controls="searchOptions" role="button" aria-expanded="false" data-bs-toggle="collapse"><fmt:message key="aside_menu.filters"/></a>
            </div>
        </div>
        <div id="searchOptions" class="collapse-button collapse">
            <%--PRICE RANGE--%>
            <div class="card-header"><fmt:message key="aside_menu.price"/></div>
            <div class="card-body">
                <input current-max-price="${currentMaxPrice}"
                       current-min-price="${currentMinPrice}"
                       max-price="${requestScope.maxPrice}"
                       min-price="${requestScope.minPrice}" type="text" id="rangePrimary" name="rangePrimary" value=""/>
                <p id="priceRangeSelected"></P>
            </div>
            <%--/PRICE RANGE--%>
            <%-- CATEGORIES --%>
            <div class="card-header"><fmt:message key="aside_menu.categories"/></div>
            <div class="card-body">
                <c:forEach items="${requestScope.categories}" var="c">
                    <div class="form-check">
                        <label class="form-check-label float-start" for="ctg">
                                ${c.name}
                        </label>
                        <c:choose>
                            <c:when test="${ctgs.contains(c.id)}">
                                <input class="form-check-input float-end" type="checkbox" name="ctg" value="${c.id}"
                                       id="ctg" checked>
                            </c:when>
                            <c:otherwise>
                                <input class="form-check-input float-end" type="checkbox" name="ctg" value="${c.id}"
                                       id="ctg">
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>
            <%-- /CATEGORIES --%>
            <%-- PRODUCERS --%>
            <div class="card-header"><fmt:message key="aside_menu.producers"/></div>
            <div class="card-body">
                <div class="form-group">
                    <c:forEach items="${requestScope.producers}" var="p">
                        <div class="form-check">
                            <label class="form-check-label float-start" for="prod">
                                    ${p.name}
                            </label>
                            <c:choose>
                                <c:when test="${prods.contains(p.id)}">
                                    <input class="form-check-input float-end" type="checkbox" name="prod"
                                           value="${p.id}"
                                           id="prod" checked>
                                </c:when>
                                <c:otherwise>
                                    <input class="form-check-input float-end" type="checkbox" name="prod"
                                           value="${p.id}"
                                           id="prod">
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </c:forEach>
                </div>
            </div>
            <%--/PRODUCERS --%>
        </div>
    </div>
    <%--/FILTERS--%>
    <%--SORTING--%>
    <select class="form-select mt-2" name="sortMethod"
            onchange='if(this.value !== 0) { this.form.submit(); }'>
        <option value="" selected disabled hidden><fmt:message key="aside_menu.sorting"/></option>
        <c:choose>
            <c:when test="${sortMethod == 'name'}">
                <option value="name-reverse"><fmt:message key="aside_menu.sorting.by_name"/> <fmt:message key="aside_menu.sorting.reverse_order"/></option>
            </c:when>
            <c:otherwise>
                <option value="name"><fmt:message key="aside_menu.sorting.by_name"/></option>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${sortMethod == 'price'}">
                <option value="price-reverse"><fmt:message key="aside_menu.sorting.by_price"/> <fmt:message key="aside_menu.sorting.reverse_order"/></option>
            </c:when>
            <c:otherwise>
                <option value="price"><fmt:message key="aside_menu.sorting.by_price"/></option>
            </c:otherwise>
        </c:choose>
    </select>
    <%--/SORTING--%>
</form>
<%--AMOUNT OF PRODUCTS ON PAGE--%>
<form action="/amount_of_products" method="post" class="mt-2">
    <div class="input-group">
        <input type="text" name="product-amount" class="form-control" placeholder="<fmt:message key="aside_menu.amount"/>"
               value="${sessionScope.productsPageAmount}">
        <button class="btn btn-success" type="submit">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                 class="bi bi-border-all" viewBox="0 0 16 16">
                <path d="M0 0h16v16H0V0zm1 1v6.5h6.5V1H1zm7.5 0v6.5H15V1H8.5zM15 8.5H8.5V15H15V8.5zM7.5 15V8.5H1V15h6.5z"/>
            </svg>
            <path fill-rule="evenodd"
                  d="M10.442 10.442a1 1 0 0 1 1.415 0l3.85 3.85a1 1 0 0 1-1.414 1.415l-3.85-3.85a1 1 0 0 1 0-1.415z"></path>
            <path fill-rule="evenodd"
                  d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zM13 6.5a6.5 6.5 0 1 1-13 0 6.5 6.5 0 0 1 13 0z"></path>
            </svg>
        </button>
    </div>
</form>
<%--/AMOUNT OF PRODUCTS ON PAGE--%>