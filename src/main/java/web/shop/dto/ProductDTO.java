package web.shop.dto;

public class ProductDTO {

    private String name;
    private Integer price;
    private String path;
    private Integer availableAmount;
    private Integer currentAmount;

    public String getName() {
        return name;
    }

    public ProductDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public ProductDTO setPrice(Integer price) {
        this.price = price;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ProductDTO setPath(String path) {
        this.path = path;
        return this;
    }

    public Integer getAvailableAmount() {
        return availableAmount;
    }

    public ProductDTO setAvailableAmount(Integer availableAmount) {
        this.availableAmount = availableAmount;
        return this;
    }

    public Integer getCurrentAmount() {
        return currentAmount;
    }

    public ProductDTO setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
        return this;
    }
}
