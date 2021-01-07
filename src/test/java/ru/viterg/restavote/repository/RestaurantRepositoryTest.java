package ru.viterg.restavote.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.viterg.restavote.model.Restaurant;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.viterg.restavote.RestaurantsTestData.*;

public class RestaurantRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Test
    void delete() {
        restaurantRepository.delete(restaurant1.getId());
        assertNull(restaurantRepository.get(restaurant1.getId()));
    }

    @Test
    void deleteNotFound() {
        assertFalse(restaurantRepository.delete(NOT_FOUND));
    }

    @Test
    void create() {
        Restaurant created = restaurantRepository.save(getNewRestaurant());
        int newId = created.id();
        Restaurant newRestaurant = getNewRestaurant();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.get(newId), newRestaurant);
    }

    @Test
    void get() {
        Restaurant actual = restaurantRepository.get(restaurant1.getId());
        RESTAURANT_MATCHER.assertMatch(actual, restaurant1);
    }

    @Test
    void getNotFound() {
        assertNull(restaurantRepository.get(NOT_FOUND));
    }

    @Test
    void update() {
        Restaurant updated = getUpdatedRestaurant();
        restaurantRepository.save(updated);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.get(RESTAURANT_START_ID), getUpdatedRestaurant());
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.getAll(), restaurants);
    }

    @Test
    void createWithException() {
        validateRootCause(() -> restaurantRepository.save(new Restaurant(null, "  ")), ConstraintViolationException.class);
        validateRootCause(() -> restaurantRepository.save(new Restaurant(null, "")), ConstraintViolationException.class);
    }
}