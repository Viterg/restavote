package ru.viterg.restavote.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.viterg.restavote.RestaurantsTestData.*;

public class DishRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    protected DishRepository dishRepository;

    @Test
    void delete() {
        dishRepository.delete(dish1.getId(), restaurant1.getId());
        assertThrows(NotFoundException.class, () -> dishRepository.get(dish1.getId(), restaurant1.getId()));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> dishRepository.delete(NOT_FOUND, restaurant1.getId()));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> dishRepository.delete(dish1.getId(), restaurant3.getId()));
    }

    @Test
    void create() {
        Dish created = dishRepository.save(getNewDish(), restaurant1.getId());
        int newId = created.id();
        Dish newMeal = getNewDish();
        newMeal.setId(newId);
        DISH_MATCHER.assertMatch(created, newMeal);
        DISH_MATCHER.assertMatch(dishRepository.get(newId, restaurant1.getId()), newMeal);
    }

    @Test
    void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () -> dishRepository.save(
                new Dish(null, dish1.getDay(), "duplicate", 100), restaurant1.getId()));
    }

    @Test
    void get() {
        Dish actual = dishRepository.get(dish1.getId(), restaurant1.getId());
        DISH_MATCHER.assertMatch(actual, dish1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> dishRepository.get(NOT_FOUND, restaurant1.getId()));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> dishRepository.get(dish1.getId(), restaurant3.getId()));
    }

    @Test
    void update() {
        Dish updated = getUpdatedDish();
        dishRepository.save(updated, restaurant1.getId());
        DISH_MATCHER.assertMatch(dishRepository.get(dish1.getId(), restaurant1.getId()), getUpdatedDish());
    }

    @Test
    void updateNotOwn() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                                                   () -> dishRepository.save(dish1, restaurant3.getId()));
        Assertions.assertEquals("Not found entity with id=" + dish1.getId(), exception.getMessage());
        DISH_MATCHER.assertMatch(dishRepository.get(dish1.getId(), restaurant1.getId()), dish1);
    }

    @Test
    void getAll() {
        DISH_MATCHER.assertMatch(dishRepository.getAll(restaurant1.getId()), dish1, dish4);
    }

    @Test
    void getMenuOfDay() {
        DISH_MATCHER.assertMatch(
                dishRepository.getMenuOfDay(LocalDate.of(2020, Month.JANUARY, 30), restaurant1.getId()), dish1);
    }

    @Test
    void getMenuOfNullDay() {
        DISH_MATCHER.assertMatch(dishRepository.getMenuOfDay(null, restaurant1.getId()), Collections.emptyList());
    }

    @Test
    void createWithException() {
        validateRootCause(() -> dishRepository.save(new Dish(null, LocalDate.of(2015, Month.JUNE, 1), "  ", 300), restaurant1.getId()), ConstraintViolationException.class);
        validateRootCause(() -> dishRepository.save(new Dish(null, null, "Description", 300), restaurant1.getId()), ConstraintViolationException.class);
        validateRootCause(() -> dishRepository.save(new Dish(null, LocalDate.of(2015, Month.JUNE, 1), "Description", 9), restaurant1.getId()), ConstraintViolationException.class);
        validateRootCause(() -> dishRepository.save(new Dish(null, LocalDate.of(2015, Month.JUNE, 1), "Description", 5001), restaurant1.getId()), ConstraintViolationException.class);
    }
}