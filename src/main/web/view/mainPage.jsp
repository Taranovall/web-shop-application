<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Main page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/ion-rangeslider/2.3.1/css/ion.rangeSlider.min.css"/>
    <link rel="stylesheet" type="text/css" href="/style/productPage.css">
    <link rel="stylesheet" type="text/css" href="/style/image.css">
    <script defer src="/js/priceSlider.js"></script>
    <script defer src="/js/disableAddingToCart.js"></script>
    <script defer src="/js/cart.js"></script>
</head>
<body>
<t:navbar/>
<div class="container-fluid">
    <div class="row">
        <c:if test="${requestScope.orderMade != null}">
            <div class="alert alert-success" role="alert">
                    ${requestScope.orderMade}
            </div>
        </c:if>
        <aside class="col-xs-12 col-sm-4 col-md-3 col-lg-2">
            <t:asideMenu/>
        </aside>
        <main class="col-xs-12 col-sm-8 col-md-9 col-lg-10 row mb-2">
            <c:choose>
                <c:when test="${error != null}">
                    <p>
                    <h1>${error}</h1></p>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${requestScope.products}" var="p">
                        <t:product product="${p}"/>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </main>
        <t:pagnation/>
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
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
        integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/ion-rangeslider/2.3.1/js/ion.rangeSlider.min.js">
</script>
</html>
