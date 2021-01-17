package ru.viterg.restavote.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.model.Restaurant;
import ru.viterg.restavote.repository.DishRepository;
import ru.viterg.restavote.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {
    private final DishRepository       dishRepository;
    private final RestaurantRepository restaurantRepository;

    public DishService(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Dish save(Dish dish, int restId) {
        if (!dish.isNew() && dishRepository.get(dish.getId(), restId).isEmpty()) {
            return null;
        }
        dish.setRestaurant(restaurantRepository.getOne(restId));
        return dishRepository.save(dish);
    }

    public Dish get(int id, int restId) {
        Dish dish = dishRepository.getExisted(id);
        return dish.getRestaurant().getId() == restId ? dish : null;
    }

    public List<Dish> getAll(int restId) {
        return dishRepository.getAllByRestaurantId(restId);
    }

    public boolean delete(int id, int restId) {
        return dishRepository.delete(id, restId) != 0;
    }

    public List<Dish> getMenuOfDay(LocalDate day, int restId) {
        return dishRepository.getAllByDayAndRestaurantId(day, restId);
    }

    @Transactional
    public List<Restaurant> getAllByDay(LocalDate day) {
        return dishRepository.getAllByDay(day).stream()
                             .map(Dish::getRestaurant)
                             .collect(Collectors.toList());
    }
}