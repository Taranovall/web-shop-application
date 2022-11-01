package web.shop.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.shop.exception.ProductNotFoundException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartTest {

    private Cart cart;
    private Map<Product, Integer> cartMap;
    private final Product firstProduct = new Product().setId(1L).setName("FirstProduct").setPrice(500).setAmount(6);
    private final Product secondProduct = new Product().setId(2L).setName("SecondProduct").setPrice(1000).setAmount(12);


    @BeforeEach
    void setUp() {
        this.cartMap = new HashMap<>();
        this.cart = new Cart(cartMap);
    }

    @Test
    void shouldPutProductsInCart() {
        cart.putProduct(firstProduct);

        Integer expectedAmountOfProducts = 1;
        Integer expectedMapSize = 1;
        Integer actualAmountOfProducts = cart.getAmountOfProducts();
        Integer actualMapSize = cartMap.size();

        assertEquals(expectedAmountOfProducts, actualAmountOfProducts);
        assertEquals(expectedMapSize, actualMapSize);

        cart.putProduct(firstProduct);
        cart.putProduct(secondProduct);

        expectedAmountOfProducts = 3;
        expectedMapSize = 2;
        actualAmountOfProducts = cart.getAmountOfProducts();
        actualMapSize = cartMap.size();

        assertEquals(expectedAmountOfProducts, actualAmountOfProducts);
        assertEquals(expectedMapSize, actualMapSize);
    }

    @Test
    void shouldRemoveProductFromCart() throws ProductNotFoundException {
        cart.putProduct(firstProduct);
        cart.putProduct(secondProduct);

        Integer expectedAmountOfProducts = 2;
        Integer actualAmountOfProducts = cart.getAmountOfProducts();

        assertEquals(expectedAmountOfProducts, actualAmountOfProducts);

        cart.removeProduct(1L);

        expectedAmountOfProducts = 1;
        actualAmountOfProducts = cart.getAmountOfProducts();

        assertEquals(expectedAmountOfProducts, actualAmountOfProducts);
    }

    @Test
    void shouldReturnMapOfProductsPutInCart() {
        this.cartMap = new LinkedHashMap<>();
        cart.putProduct(secondProduct);
        cart.putProduct(firstProduct);
        cart.putProduct(firstProduct);


        Map<Product, Integer> expectedMap = Map.of(
                secondProduct, 1,
                firstProduct, 2
        );
        Map<Product, Integer> actualMap = cart.getProducts();

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void shouldGetTotalPriceAndAmountOfProducts() {
        int amountOfFirstProduct = 4;
        int amountOfSecondProduct = 9;
        for (int i = 0; i < amountOfFirstProduct; i++) {
            cart.putProduct(firstProduct);
        }
        for (int i = 0; i < amountOfSecondProduct; i++) {
            cart.putProduct(secondProduct);
        }

        Integer expectedAmountOfProducts = amountOfFirstProduct + amountOfSecondProduct;
        Integer expectedTotalPrice = (amountOfFirstProduct * firstProduct.getPrice())
                + (amountOfSecondProduct * secondProduct.getPrice());
        Integer actualAmountOfProducts = cart.getAmountOfProducts();
        Integer actualTotalPrice = cart.getTotalPrice();

        assertEquals(expectedAmountOfProducts, actualAmountOfProducts);
        assertEquals(expectedTotalPrice, actualTotalPrice);
    }
}