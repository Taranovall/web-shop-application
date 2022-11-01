package web.shop.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Constraint {

    private final String urlPattern;
    private final List<String> roles;

    public Constraint(String urlPattern, List<String> roles) {
        this.urlPattern = urlPattern;
        this.roles = new LinkedList<>(roles);
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Constraint)) {
            return false;
        }
        Constraint that = (Constraint) o;
        return Objects.equals(urlPattern, that.urlPattern) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(urlPattern, roles);
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "urlPattern='" + urlPattern + '\'' +
                ", roles=" + roles +
                '}';
    }
}
