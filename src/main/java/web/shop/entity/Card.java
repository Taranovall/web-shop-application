package web.shop.entity;

import java.time.YearMonth;
import java.util.Objects;

public class Card {

    private String number;
    private Integer cvv;
    private YearMonth expirationDate;

    public Card() {
    }

    public String getNumber() {
        return number;
    }

    public Card setNumber(String number) {
        this.number = number;
        return this;
    }

    public Integer getCvv() {
        return cvv;
    }

    public Card setCvv(Integer cvv) {
        this.cvv = cvv;
        return this;
    }

    public YearMonth getExpirationDate() {
        return expirationDate;
    }

    public Card setExpirationDate(YearMonth expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        Card card = (Card) o;
        return Objects.equals(number, card.number) && Objects.equals(cvv, card.cvv) && Objects.equals(expirationDate, card.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, cvv, expirationDate);
    }

    @Override
    public String toString() {
        return "Card{" +
                "number='" + number + '\'' +
                ", cvv=" + cvv +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
