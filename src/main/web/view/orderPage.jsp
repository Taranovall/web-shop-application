<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set value="${sessionScope.cart.getProducts()}" var="cart" scope="page"/>
<c:set var="count" value="0" scope="page"/>
<html>
<head>
    <title><fmt:message key="order"/><</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="/style/productPage.css">
    <link rel="stylesheet" type="text/css" href="/style/order.css">
    <script defer src="/js/displayPaymentInformation.js"></script>
</head>
<body>
<t:navbar/>
<div class="container-fluid">
    <div class="row mt-2">
        <div class="col-xs-12 col-sm-8">
            <c:choose>
                <c:when test="${sessionScope.user == null}">
                    <div class="alert alert-danger" role="alert" id="error">
                            ${requestScope.error}
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="input-group">
                        <input type="text" aria-label="First name" class="form-control" disabled
                               value="${sessionScope.user.firstName}">
                        <input type="text" aria-label="Last name" class="form-control" disabled
                               value="${sessionScope.user.secondName}">
                    </div>
                    <form action="/make-order" id="buyerData" method="POST">
                        <div class="col-12 mt-2">
                            <input type="text" class="form-control" placeholder="<fmt:message key="order_menu.address"/>" name="address"/>
                        </div>
                        <div class="mt-2">
                            <fmt:message key="order_menu.delivery_type"/>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="delivery" id="courier" value="courier"
                                       checked>
                                <label class="form-check-label" for="courier">
                                    <fmt:message key="order_menu.delivery_type.courier"/>
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="delivery" id="mailing" value="mailing">
                                <label class="form-check-label" for="mailing">
                                    <fmt:message key="order_menu.delivery_type.mail"/>
                                </label>
                            </div>
                        </div>
                        <div class="mt-2">
                            <fmt:message key="order_menu.payment_method"/>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="payment" id="cash" value="cash"
                                       checked>
                                <label class="form-check-label" for="cash">
                                    <fmt:message key="order_menu.payment_method.cash"/>
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="payment" id="card" value="card">
                                <label class="form-check-label" for="card">
                                    <fmt:message key="order_menu.payment_method.card"/>
                                </label>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-outline-dark w-100 mt-2" id="buy"><fmt:message key="order_menu.confirm"/></button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="col-xs-12 col-sm-4 row mb-2">
            <div class="table-container">
                <table class="table">
                    <thead>
                    <tr class="text-center">
                        <th scope="col">#</th>
                        <th scope="col"><fmt:message key="order_menu.table.name"/></th>
                        <th scope="col"><fmt:message key="order_menu.table.image"/></th>
                        <th scope="col"><fmt:message key="order_menu.table.amount"/></th>
                        <th scope="col"><fmt:message key="order_menu.table.price"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="ordered-product-wrapper">
                        <c:forEach items="${cart}" var="e">
                            <c:set var="count" value="${count + 1}" scope="page"/>
                    <tr>
                        <th scope="row">${count}</th>
                        <td>${e.key.name}</td>
                        <td><img class="image" src="${e.key.imgPath}" alt=""></td>
                        <td>${e.value}</td>
                        <td>${e.key.price * e.value} UAH</td>
                    </tr>
                    </c:forEach>
                    </tr>
                    </tbody>
                </table>
                <div class="alert alert-dark text-center" style="font-weight: 700;" role="alert">
                    <fmt:message key="cart.total_price"/> : ${sessionScope.cart.getTotalPrice()} UAH
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script
        src="https://code.jquery.com/jquery-3.6.1.js"
        integrity="sha256-3zlB5s2uwoUzrXK3BT7AX3FyvojsraNFxCc2vC/7pNI="
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
        integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
        crossorigin="anonymous"></script>
</html>
