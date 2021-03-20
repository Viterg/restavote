package ru.viterg.restavote.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.viterg.restavote.AbstractDataManageTest;
import ru.viterg.restavote.model.Dish;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.viterg.restavote.RestaurantsTestData.*;

public class DishRepositoryTest extends AbstractDataManageTest {

    @Autowired
    protected DishRepository dishRepository;

    @Test
    void delete() {
        dishRepository.delete(dish1.getId(), restaurant1.getId());
        assertTrue(dishRepository.get(dish1.getId(), restaurant1.getId()).isEmpty());
    }

    @Test
    void deleteNotFound() {
        assertEquals(0, dishRepository.delete(NOT_FOUND, restaurant1.getId()));
    }

    @Test
    void deleteNotOwn() {
        assertEquals(0, dishRepository.delete(dish1.getId(), restaurant3.getId()));
    }

    @Test
    void get() {
        DISH_MATCHER.assertMatch(dishRepository.getExisted(dish1.getId()), dish1);
    }

    @Test
    void getNotFound() {
        assertTrue(dishRepository.get(NOT_FOUND, restaurant1.getId()).isEmpty());
    }

    @Test
    void getNotOwn() {
        assertTrue(dishRepository.get(dish1.getId(), restaurant3.getId()).isEmpty());
    }

    @Test
    void getAll() {
        DISH_MATCHER.assertMatch(dishRepository.getAllByRestaurantId(restaurant1.getId()), dish1, dish4);
    }

    @Test
    void getMenuOfDay() {
        LocalDate day = LocalDate.of(2020, Month.DECEMBER, 30);
        List<Dish> menuOfDay = dishRepository.getAllByDayAndRestaurantId(day, restaurant1.getId());
        DISH_MATCHER.assertMatch(menuOfDay, dish1);
    }

    @Test
    void getMenuOfNullDay() {
        List<Dish> menuOfDay = dishRepository.getAllByDayAndRestaurantId(null, restaurant1.getId());
        DISH_MATCHER.assertMatch(menuOfDay, Collections.emptyList());
    }
}