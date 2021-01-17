package ru.viterg.restavote.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.viterg.restavote.AbstractDataManageTest;
import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.model.Restaurant;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.viterg.restavote.RestaurantsTestData.*;

class DishServiceTest extends AbstractDataManageTest {

    @Autowired
    protected DishService service;

    @Test
    void create() {
        Dish created = service.save(getNewDish(), restaurant1.getId());
        int newId = created.id();
        Dish newMeal = getNewDish();
        newMeal.setId(newId);
        DISH_MATCHER.assertMatch(created, newMeal);
        DISH_MATCHER.assertMatch(service.get(newId, restaurant1.getId()), newMeal);
    }

    @Test
    void update() {
        Dish updated = getUpdatedDish();
        service.save(updated, restaurant1.getId());
        DISH_MATCHER.assertMatch(service.get(dish1.getId(), restaurant1.getId()), getUpdatedDish());
    }

    @Test
    void updateNotOwn() {
        assertNull(service.save(dish1, restaurant3.getId()));
        DISH_MATCHER.assertMatch(service.get(dish1.getId(), restaurant1.getId()), dish1);
    }

    @Test
    void getAllByDay() {
        service.save(getNewDish(), restaurant1.getId());
        List<Restaurant> allByDay = service.getAllByDay(now());
        RESTAURANT_MATCHER.assertMatch(allByDay, List.of(restaurant1));
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> {
            Dish dish = new Dish(null, LocalDate.of(2015, Month.JUNE, 1), "  ", 300);
            service.save(dish, restaurant1.getId());
        });
        validateRootCause(ConstraintViolationException.class, () -> {
            Dish dish = new Dish(null, null, "Description", 300);
            service.save(dish, restaurant1.getId());
        });
    }
}