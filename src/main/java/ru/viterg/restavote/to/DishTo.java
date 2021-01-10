package ru.viterg.restavote.to;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.Objects;

public class DishTo extends BaseTo {

    private final LocalDate day;
    private final String    description;
    private final int           price;

    @ConstructorProperties({"id", "dateTime", "description", "price"})
    public DishTo(Integer id, LocalDate day, String description, int price) {
        super(id);
        this.day = day;
        this.description = description;
        this.price = price;
    }

    public LocalDate getDay() {
        return day;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishTo dishTo = (DishTo) o;
        return price == dishTo.price &&
               Objects.equals(id, dishTo.id) &&
               Objects.equals(day, dishTo.day) &&
               Objects.equals(description, dishTo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day, description, price);
    }

    @Override
    public String toString() {
        return "DishTo{" +
               "id=" + id +
               ", dateTime=" + day +
               ", description='" + description + '\'' +
               ", calories=" + price +
               '}';
    }
}
