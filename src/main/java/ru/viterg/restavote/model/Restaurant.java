package ru.viterg.restavote.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "restaurants", uniqueConstraints = @UniqueConstraint(columnNames = "description", name = "rest_unique_idx"))
public class Restaurant extends AbstractBaseEntity {

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 1, max = 255)
    private String description;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("day DESC")
    @JsonIgnore
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Vote> votes;

    public Restaurant() {
    }

    public Restaurant(String description) {
        this(null, description);
    }

    public Restaurant(Integer id, String description) {
        super(id);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
               "description='" + description + '\'' +
               ", id=" + id +
               '}';
    }
}