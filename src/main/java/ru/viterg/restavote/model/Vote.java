package ru.viterg.restavote.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes")
public class Vote extends AbstractBaseEntity {

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "vote_date")
    private LocalDate voteDate;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rest_id")
    private Restaurant restaurant;

    public Vote() {
    }

    // public Vote(Vote v) {
    //     super(v.getId());
    // }

    public Vote(Integer id, User user, LocalDate voteDate, Restaurant restaurant) {
        super(id);
        this.user = user;
        this.voteDate = voteDate;
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDate voteDate) {
        this.voteDate = voteDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
               "id=" + id +
               "user=" + user +
               ", voteDate=" + voteDate +
               ", restaurant=" + restaurant +
               '}';
    }
}