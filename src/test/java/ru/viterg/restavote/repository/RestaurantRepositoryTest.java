package ru.viterg.restavote.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.viterg.restavote.AbstractDataManageTest;
import ru.viterg.restavote.model.Restaurant;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.viterg.restavote.RestaurantsTestData.*;

public class RestaurantRepositoryTest extends AbstractDataManageTest {

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Test
    void delete() {
        restaurantRepository.delete(restaurant1.getId());
        assertTrue(restaurantRepository.findById(restaurant1.getId()).isEmpty());
    }

    @Test
    void deleteNotFound() {
        assertEquals(0, restaurantRepository.delete(NOT_FOUND));
    }

    @Test
    void create() {
        Restaurant created = restaurantRepository.save(getNewRestaurant());
        int newId = created.id();
        Restaurant newRestaurant = getNewRestaurant();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findById(newId).orElse(null), newRestaurant);
    }

    @Test
    void get() {
        Restaurant actual = restaurantRepository.findById(restaurant1.getId()).orElse(null);
        RESTAURANT_MATCHER.assertMatch(actual, restaurant1);
    }

    @Test
    void getNotFound() {
        assertTrue(restaurantRepository.findById(NOT_FOUND).isEmpty());
    }

    @Test
    void update() {
        Restaurant updated = getUpdatedRestaurant();
        restaurantRepository.save(updated);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findById(RESTAURANT_START_ID).orElse(null),
                                       getUpdatedRestaurant());
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findAll(), restaurants);
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class,
                          () -> restaurantRepository.save(new Restaurant(null, "  ")));
        validateRootCause(ConstraintViolationException.class,
                          () -> restaurantRepository.save(new Restaurant(null, "")));
    }
}