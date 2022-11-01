package web.shop.entity;

import java.util.Objects;

public class Producer {

    private Long id;
    private String name;

    public Producer() {
    }

    public Long getId() {
        return id;
    }

    public Producer setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Producer setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producer)) {
            return false;
        }
        Producer producer = (Producer) o;
        return Objects.equals(id, producer.id) && Objects.equals(name, producer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
