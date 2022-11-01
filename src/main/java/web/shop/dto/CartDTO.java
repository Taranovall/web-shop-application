package web.shop.dto;

public class CartDTO {

    Integer amountOfProducts;
    Integer totalPrice;

    public Integer getAmountOfProducts() {
        return amountOfProducts;
    }

    public CartDTO setAmountOfProducts(Integer amountOfProducts) {
        this.amountOfProducts = amountOfProducts;
        return this;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public CartDTO setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }
}
