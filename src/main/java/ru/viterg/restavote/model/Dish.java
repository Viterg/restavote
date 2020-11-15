package ru.viterg.restavote.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.*;

@NamedQueries({
    @NamedQuery(name = Dish.ALL_SORTED, query = "SELECT d FROM Dish d WHERE d.restaurant.id=:restId ORDER BY d.dateTime DESC"),
    @NamedQuery(name = Dish.DELETE, query = "DELETE FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restId"),
    @NamedQuery(name = Dish.GET_BETWEEN, query = """
                            SELECT d FROM Dish d WHERE d.restaurant.id=:restId AND d.dateTime >= :startDateTime
                            AND d.dateTime < :endDateTime ORDER BY d.dateTime DESC"""),
              })
@Entity
@Table(name = "dishes", uniqueConstraints = @UniqueConstraint(columnNames = {"rest_id", "date_time"},
                                                              name = "meals_unique_rest_datetime_idx"))
public class Dish extends AbstractBaseEntity {
    public static final String ALL_SORTED  = "Dish.getAll";
    public static final String DELETE      = "Dish.delete";
    public static final String GET_BETWEEN = "Dish.getBetween";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 1, max = 255)
    private String description;

    @Column(name = "price", nullable = false)
    @Min(1)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(LocalDateTime dateTime, String description, int price) {
        this(null, dateTime, description, price);
    }

    public Dish(Integer id, LocalDateTime dateTime, String description, int price) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.price = price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
               ", dateTime=" + dateTime +
               ", description='" + description + '\'' +
               ", calories=" + price +
               '}';
    }
}