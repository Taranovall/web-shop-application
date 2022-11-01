package web.shop.entity;

import java.util.Objects;

public class OrderedProduct {

    private final Product product;
    private final Integer price;
    private final Integer amount;

    public OrderedProduct(Product product, Integer price, Integer amount) {
        this.product = new Product()
                .setId(product.getId())
                .setName(product.getName())
                .setPrice(product.getPrice())
                .setAmount(product.getAmount())
                .setProducer(product.getProducer())
                .setCategory(product.getCategory())
                .setDescription(product.getDescription())
                .setImgPath(product.getImgPath());
        this.price = price;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderedProduct)) {
            return false;
        }
        OrderedProduct that = (OrderedProduct) o;
        return Objects.equals(product, that.product) && Objects.equals(price, that.price) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, price, amount);
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "OrderedProduct{" +
                "product=" + product +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
