package ru.viterg.restavote.repository;

import ru.viterg.restavote.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    List<Restaurant> getAll();

    // null if restaurant do not belong to userId
    Restaurant get(int id, int userId);

    // null if updated restaurant do not belong to userId
    Restaurant save(Restaurant restaurant, int userId);

}