package ru.viterg.restavote;

import ru.viterg.restavote.model.*;

import java.time.Month;
import java.util.List;

import static java.time.LocalDate.*;
import static ru.viterg.restavote.UserTestData.user1;
import static ru.viterg.restavote.model.AbstractBaseEntity.START_SEQ;

public class RestaurantsTestData {
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "dishes", "votes");
    public static final TestMatcher<Dish>       DISH_MATCHER       = TestMatcher.usingIgnoringFieldsComparator(Dish.class, "restaurant");
    public static final TestMatcher<Vote>       VOTE_MATCHER       = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "id", "user", "restaurant");

    public static final int NOT_FOUND           = 10;
    public static final int RESTAURANT_START_ID = START_SEQ + 3;
    public static final int DISH_START_ID       = START_SEQ + 6;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_START_ID, "Метрополь");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_START_ID + 1, "Пронтера");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT_START_ID + 2, "Папа Джонс");

    public static final Dish dish1 = new Dish(DISH_START_ID, of(2020, Month.JANUARY, 30), "Завтрак", 500);
    public static final Dish dish2 = new Dish(DISH_START_ID + 1, of(2020, Month.JANUARY, 30), "Обед", 1000);
    public static final Dish dish3 = new Dish(DISH_START_ID + 2, of(2020, Month.JANUARY, 30), "Ужин", 500);
    public static final Dish dish4 = new Dish(DISH_START_ID + 3, of(2020, Month.JANUARY, 31), "Завтрак", 500);
    public static final Dish dish5 = new Dish(DISH_START_ID + 4, of(2020, Month.JANUARY, 31), "Обед", 1000);
    public static final Dish dish6 = new Dish(DISH_START_ID + 5, of(2020, Month.JANUARY, 31), "Ужин", 510);

    public static final Vote vote1 = new Vote(DISH_START_ID + 6, user1, of(2020, Month.JUNE, 12), restaurant3);

    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant2, restaurant3);
    public static final List<Dish>       dishes      = List.of(dish6, dish5, dish4, dish3, dish2, dish1);

    public static Restaurant getNewRestaurant() {
        return new Restaurant(null, "Albion");
    }

    public static Restaurant getUpdatedRestaurant() {
        return new Restaurant(RESTAURANT_START_ID, "Pronto");
    }

    public static Dish getNewDish() {
        return new Dish(null, now(), "Созданный ужин", 300);
    }

    public static Dish getUpdatedDish() {
        return new Dish(DISH_START_ID, dish1.getDay().minusDays(2), "Обновленный завтрак", 200);
    }

    public static Vote getNewVote() {
        return new Vote(null, user1, now(), restaurant1);
    }

    public static Vote getUpdatedVote() {
        return new Vote(DISH_START_ID + 6, user1, now(), restaurant2);
    }
}