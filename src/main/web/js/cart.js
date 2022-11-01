$(document).ready(function () {
    $("button[name='add-product']").on('click', function () {
        const id = $(this).attr("data-id");

        function addProductToCart($wrapper, $jsonProduct) {
            $wrapper.append($('<li>').attr('class', "row position").attr('id', "product-" + id)
                .append($('<div>').attr('class', 'col-md-4').append($('<img alt="">').attr('src', $jsonProduct.path)))
                .append($('<div>').attr('class', 'col-md-6').text($jsonProduct.name).append($('<div>').attr('class', 'price').text($jsonProduct.price + " UAH")))
                .append($('<div>').attr('class', 'col-md-2 amount')
                    .append('<a class="btn-outline-dark" id="remove-product" data-id="' + id + '">\n' +
                        '                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"\n' +
                        '                                                     fill="currentColor" class="bi bi-dash-square"\n' +
                        '                                                     viewBox="0 0 16 16">\n' +
                        '                                                    <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>\n' +
                        '                                                    <path d="M4 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 4 8z"/>\n' +
                        '                                                </svg>\n' +
                        '                                            </a>')
                    .append($('<input>').attr('name', 'amount-cart').attr('type', 'number').attr('value', '1').attr('min', '1').attr("max", $jsonProduct.availableAmount).attr('data-id', id))));
        }

        $.ajax({
            type: 'GET',
            url: '/cart/' + id,
            success: function (responseText) {
                let $json = $.parseJSON(responseText);
                let $jsonProduct = $json.product;
                updateCartInfo($json.cart)
                let $wrapper = $('#wrapper');
                let $productInCart = $wrapper.children("#product-" + id);
                if (!$productInCart.length) {
                    addProductToCart($wrapper, $jsonProduct);
                } else {
                    $productInCart.find("input").val($jsonProduct.currentAmount);
                }
            }
        });
    })
});

$(document).on('click', '#remove-product', function () {
    const id = $(this).attr("data-id");
    $.ajax({
        url: '/cart/',
        type: 'POST',
        data: {productId: id},
        success: function (responseText) {
            let $cart = $.parseJSON(responseText);
            if ($cart.amountOfProducts === 0) {
                $("span[class='total-products-amount']").remove();
                $("ul[class='dropdown-menu']").children().remove();
            } else {
                $('#product-' + id).remove();
                updateCartInfo($cart);
            }
        }
    })
})

$(document).on('change', "input[name='amount-cart']", function () {
    const id = $(this).attr("data-id");
    const amount = $(this).val();
    $.ajax({
        url: '/cart/?productId=' + id + "&amount=" + amount,
        type: 'PUT',
        success: function (responseText) {
            let $json = $.parseJSON(responseText);
            updateCartInfo($json);
        }
    });
});

function updateCartInfo($jsonCart) {
    let $amountIsEmpty = $("span[id='product-amount']").length === 0 && $jsonCart.amountOfProducts > 0;
    let $div = ($("li[class='cart']").children());

    function addAmountOfProductsToCart() {
        let $span = $('<span />')
            .addClass('total-products-amount')
            .attr('id', 'product-amount')
            .html($jsonCart.amountOfProducts);
        $div.prepend($span);
    }

    function createCartContent() {
        let $buyButton = $('#buyButton').val();
        let $totalPrice = $('#totalPrice').val()
        let $priceDiv = $('<div/>').addClass("text-center total-price").prepend('<span>' + $totalPrice + ' <span id="total-price">' + $jsonCart.totalPrice + '</span> UAH</span>');
        let $dropdownMenu = $("ul[class='dropdown-menu']");
        $dropdownMenu.append($priceDiv);
        $dropdownMenu.append('<div class="cart-wrapper" id="wrapper">');
        $dropdownMenu.append('<div class="text-center buy">\n' +
            '                        <a href="/make-order" class="btn btn-outline-dark">' + $buyButton + '</a>\n' +
            '                    </div>');
    }

    if ($amountIsEmpty) {
        addAmountOfProductsToCart();
        createCartContent();
    } else {
        $("#product-amount").text($jsonCart.amountOfProducts);
        $("#total-price").text($jsonCart.totalPrice);
    }
}