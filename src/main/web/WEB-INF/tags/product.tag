<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="product" required="true" type="web.shop.entity.Product" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<input id="outOfStock" value="<fmt:message key="shop.out_of_stock"/>" hidden/>

<div class="col-xs-12 col-sm-6 col-md-3 mt-2 product col">
    <ul class="list-group">
        <li class="list-group-item">
            <div class="thumbnail">
                <img class="image"
                     src="${product.imgPath}"/>
                <div class="desc">
                    ${product.description}<br>
                    <fmt:message key="shop.category"/>: ${product.category.name}<br>
                    <fmt:message key="shop.producer"/>: ${product.producer.name}
                </div>
            </div>
            <div class="name">${product.name}</div>
            <div class="price">${product.price} UAH</div>
            <div class="add-to-cart">
                <input type="text" id="amount-of-product" value="${product.amount}" hidden>
                <button name="add-product" data-id="${product.id}" type="button" class="btn btn-success">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                         class="bi bi-cart" viewBox="0 0 16 16">
                        <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l1.313 7h8.17l1.313-7H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
                    </svg>
                    <fmt:message key="shop.add_to_cart"/>
                </button>
            </div>
        </li>
    </ul>
</div>