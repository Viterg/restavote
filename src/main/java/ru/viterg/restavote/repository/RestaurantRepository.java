package ru.viterg.restavote.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.Restaurant;

import java.util.List;

@Repository
public class RestaurantRepository {

    private final CrudRestaurantRepository repository;

    public RestaurantRepository(CrudRestaurantRepository repository) {
        this.repository = repository;
    }

    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    public Restaurant get(int id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public Restaurant getWithDishes(int id) {
        return repository.getWithDishes(id);
    }
}