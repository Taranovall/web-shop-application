package web.shop.bean;

public class FilterBean {

    private Integer maxPrice;
    private Integer minPrice;
    private Integer amountOfProductsOnPage;
    private Integer amountOfPages;
    private Integer currentPage;
    private String query;
    private String[] categories;
    private String[] producers;
    private String sortMethod;

    public FilterBean() {
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public FilterBean setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public FilterBean setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public Integer getAmountOfProductsOnPage() {
        return amountOfProductsOnPage;
    }

    public FilterBean setAmountOfProductsOnPage(Integer amountOfProductsOnPage) {
        this.amountOfProductsOnPage = amountOfProductsOnPage;
        return this;
    }

    public Integer getAmountOfPages() {
        return amountOfPages;
    }

    public FilterBean setAmountOfPages(Integer amountOfPages) {
        this.amountOfPages = amountOfPages;
        return this;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public FilterBean setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public FilterBean setQuery(String query) {
        this.query = query;
        return this;
    }

    public String[] getCategories() {
        return categories;
    }

    public FilterBean setCategories(String[] categories) {
        this.categories = categories;
        return this;
    }

    public String[] getProducers() {
        return producers;
    }

    public FilterBean setProducers(String[] producers) {
        this.producers = producers;
        return this;
    }

    public String getSortMethod() {
        return sortMethod;
    }

    public FilterBean setSortMethod(String sortMethod) {
        this.sortMethod = sortMethod;
        return this;
    }
}
