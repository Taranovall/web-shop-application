<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="amountOfProducts" value="${cart.getAmountOfProducts()}"/>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<input id="buyButton" value="<fmt:message key="cart.buy"/>" hidden>
<input id="totalPrice" value="<fmt:message key="cart.total_price"/>" hidden>

<li class="cart">
    <div class="btn row">
        <c:if test="${amountOfProducts > 0}">
            <span class="total-products-amount" id="product-amount">${amountOfProducts}</span>
        </c:if>
        <button type="button" id="dropdownMenuButton" class="btn btn-success" data-bs-toggle="dropdown"
                aria-expanded="false">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                 class="bi bi-basket" viewBox="0 0 16 16">
                <path d="M5.757 1.071a.5.5 0 0 1 .172.686L3.383 6h9.234L10.07 1.757a.5.5 0 1 1 .858-.514L13.783 6H15a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1v4.5a2.5 2.5 0 0 1-2.5 2.5h-9A2.5 2.5 0 0 1 1 13.5V9a1 1 0 0 1-1-1V7a1 1 0 0 1 1-1h1.217L5.07 1.243a.5.5 0 0 1 .686-.172zM2 9v4.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V9H2zM1 7v1h14V7H1zm3 3a.5.5 0 0 1 .5.5v3a.5.5 0 0 1-1 0v-3A.5.5 0 0 1 4 10zm2 0a.5.5 0 0 1 .5.5v3a.5.5 0 0 1-1 0v-3A.5.5 0 0 1 6 10zm2 0a.5.5 0 0 1 .5.5v3a.5.5 0 0 1-1 0v-3A.5.5 0 0 1 8 10zm2 0a.5.5 0 0 1 .5.5v3a.5.5 0 0 1-1 0v-3a.5.5 0 0 1 .5-.5zm2 0a.5.5 0 0 1 .5.5v3a.5.5 0 0 1-1 0v-3a.5.5 0 0 1 .5-.5z"/>
            </svg>
        </button>
        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
            <c:if test="${amountOfProducts > 0}">
                <div class="text-center total-price">
                    <span><fmt:message key="cart.total_price"/> <span id="total-price">${cart.getTotalPrice()}</span> UAH</span>
                </div>
                <div class="cart-wrapper" id="wrapper">
                    <c:forEach items="${cart.getProducts()}" var="e">
                        <li class="row position" id="product-${e.key.id}">
                            <div class="col-md-4"><img src="${e.key.imgPath}" alt=""></div>
                            <div class="col-md-6">
                                    ${e.key.name}
                                <div class="price">${e.key.price} UAH</div>
                            </div>
                            <div class="col-md-2 amount">
                                <a class="btn-outline-dark" id="remove-product" data-id="${e.key.id}">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
                                         fill="currentColor" class="bi bi-dash-square"
                                         viewBox="0 0 16 16">
                                        <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>
                                        <path d="M4 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 4 8z"/>
                                    </svg>
                                </a>
                                <input name="amount-cart" type="number" value="${e.value}" min="1" max="${e.key.amount}"
                                       data-id="${e.key.id}">
                            </div>
                        </li>
                    </c:forEach>
                </div>
                <c:if test="${amountOfProducts > 0}">
                    <div class="text-center buy">
                        <a href="/make-order" class="btn btn-outline-dark"><fmt:message key="cart.buy"/></a>
                    </div>
                </c:if>
            </c:if>
        </ul>
    </div>
</li>