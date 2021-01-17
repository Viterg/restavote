package ru.viterg.restavote.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "dishes", uniqueConstraints = @UniqueConstraint(
        columnNames = {"rest_id", "day_value", "description"}, name = "menu_unique_idx")
)
public class Dish extends AbstractBaseEntity {

    @Column(name = "day_value", nullable = false)
    @NotNull
    private LocalDate day;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 1, max = 255)
    private String description;

    @Column(name = "price", nullable = false)
    @Min(1)
    private int price; // in cents

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(LocalDate day, String description, int price) {
        this(null, day, description, price);
    }

    public Dish(Integer id, LocalDate day, String description, int price) {
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

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Dish{" +
               "id=" + id +
               ", day=" + day +
               ", description='" + description + '\'' +
               ", calories=" + price +
               '}';
    }
}