package web.shop.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private Status status;
    private String statusDetails;
    private LocalDateTime dateAndTime;
    private User client;
    private List<OrderedProduct> orderedProducts;
    private String paymentMethod;
    private String deliveryMethod;
    private String address;

    public Order(String paymentMethod, String deliveryMethod, String address) {
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.address = address;
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public Order setId(Long id) {
        this.id = id;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Order setStatus(Status status) {
        this.status = status;
        return this;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public Order setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
        return this;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public Order orderTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
        return this;
    }

    public User getClient() {
        return client;
    }

    public Order setClient(User client) {
        this.client = client;
        return this;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public Order setOrderedProducts(List<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
        return this;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Order setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public Order setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
        return this;
    }

    public Order setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Order setAddress(String address) {
        this.address = address;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) && status == order.status && Objects.equals(statusDetails, order.statusDetails) && Objects.equals(dateAndTime, order.dateAndTime) && Objects.equals(client, order.client) && Objects.equals(orderedProducts, order.orderedProducts) && Objects.equals(paymentMethod, order.paymentMethod) && Objects.equals(deliveryMethod, order.deliveryMethod) && Objects.equals(address, order.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, statusDetails, dateAndTime, client, orderedProducts, paymentMethod, deliveryMethod, address);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", statusDetails='" + statusDetails + '\'' +
                ", dateAndTime=" + dateAndTime +
                ", client=" + client +
                ", orderedProducts=" + orderedProducts +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public enum Status {
        ACCEPTED("Order is accepted, wait confirming"), CONFIRMED, BEING_FORMED, SHIPPED, COMPLETED, CANCELED;

        private String statusDetails;

        Status(String statusDetails) {
            this.statusDetails = statusDetails;
        }

        Status() {
        }

        public String getStatusDetails() {
            return statusDetails;
        }

        @Override
        public String toString() {
            return super.toString().toUpperCase();
        }
    }
}
