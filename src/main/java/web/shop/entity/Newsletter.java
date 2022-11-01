package web.shop.entity;

import web.shop.exception.not.found.NewsletterNotFoundException;

import java.util.Arrays;

public enum Newsletter {

    DISCOUNT(1L, "I want to get informed about discounts"),
    GIFT(2L, "I want to get gifts");

    Newsletter(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    private final Long id;
    private final String message;

    public String getMessage() {
        return message;
    }

    public static Newsletter getById(Long id) throws NewsletterNotFoundException {
        return Arrays.stream(Newsletter.values())
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NewsletterNotFoundException(String.format("Newsletter with ID %d doesn't exist", id)));
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
