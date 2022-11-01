package web.shop.entity;

import web.shop.exception.ProductNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Cart {

    private final Map<Product, Integer> productCart;

    public Cart() {
        this.productCart = new HashMap<>();
    }

    public Cart(Map<Product, Integer> productCart) {
        this.productCart = productCart;
    }

    public void putProduct(Product product) {
        Integer amountOfProductInCart = productCart.get(product);
        if (Objects.isNull(amountOfProductInCart) || product.getAmount() > amountOfProductInCart) {
            productCart.merge(product, 1, Integer::sum);
        }
    }

    public void removeProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> optProduct = productCart.keySet()
                .stream()
                .filter(product -> Objects.equals(product.getId(), productId))
                .findFirst();

        Product product = optProduct
                .orElseThrow(() -> new ProductNotFoundException(String.format("Cart doesn't contain product with ID %d", productId)));
        productCart.remove(product);
    }

    public Integer getAmountOfProducts() {
        return productCart.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer getTotalPrice() {
        return productCart.entrySet()
                .stream()
                .mapToInt(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }

    public void changeAmountOfProduct(Long productId, Integer amount) throws ProductNotFoundException {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be less than 0");
        }
        Optional<Product> optProduct = productCart.keySet()
                .stream()
                .filter(product -> Objects.equals(product.getId(), productId))
                .findFirst();
        Product p = optProduct
                .orElseThrow(() -> new ProductNotFoundException(String.format("Cart doesn't contain product with ID %d", productId)));
        productCart.replace(p, amount);
    }

    public Map<Product, Integer> getProducts() {
        return productCart;
    }
}
