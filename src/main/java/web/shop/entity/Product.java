package web.shop.entity;

import java.util.Objects;

public class Product {

    private Long id;
    private Integer price;
    private Integer amount;
    private String name;
    private Producer producer;
    private Category category;
    private String description;
    private String imgPath;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public Product setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public Product setPrice(Integer price) {
        this.price = price;
        return this;
    }

    public Integer getAmount() {
        return amount;
    }

    public Product setAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Producer getProducer() {
        return producer;
    }

    public Product setProducer(Producer producer) {
        this.producer = producer;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Product setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImgPath() {
        return imgPath;
    }

    public Product setImgPath(String imgPath) {
        this.imgPath = imgPath;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id.equals(product.id) && price.equals(product.price) && amount.equals(product.amount) && name.equals(product.name) && producer.equals(product.producer) && category.equals(product.category) && description.equals(product.description) && imgPath.equals(product.imgPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, amount, name, producer, category, description, imgPath);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", price=" + price +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                ", producer=" + producer +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }
}
