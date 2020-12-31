package ru.viterg.restavote.util;


import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.to.DishTo;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DishesUtil {

    private DishesUtil() {
    }

    public static List<DishTo> getTos(Collection<Dish> meals) {
        return filterByPredicate(meals, dish -> true);
    }

    public static List<DishTo> filterByPredicate(Collection<Dish> dishes, Predicate<Dish> filter) {
        return dishes.stream()
                     .filter(filter)
                     .map(DishesUtil::createTo)
                     .collect(Collectors.toList());
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getDay(), dish.getDescription(), dish.getPrice());
    }

}
