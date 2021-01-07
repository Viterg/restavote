package ru.viterg.restavote.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.model.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public class DishRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final CrudDishRepository crudDishRepository;

    public DishRepository(CrudDishRepository crudDishRepository) {
        this.crudDishRepository = crudDishRepository;
    }

    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurant(entityManager.getReference(Restaurant.class, restaurantId));
        return crudDishRepository.save(dish);
    }

    public List<Dish> getAll(int restaurantId) {
        return crudDishRepository.getAllByRestaurantId(restaurantId);
    }

    public Dish get(int id, int restaurantId) {
        return crudDishRepository.findById(id)
                                 .filter(dish -> dish.getRestaurant().getId() == restaurantId)
                                 .orElse(null);
    }

    @Transactional
    public boolean delete(int id, int restaurantId) {
        return crudDishRepository.delete(id, restaurantId) != 0;
    }

    public Dish getWithRestaurant(int id, int restId) {
        return crudDishRepository.getWithRestaurant(id, restId);
    }

    public List<Dish> getMenuOfDay(LocalDate day, int restId) {
        return crudDishRepository.getAllByDayAndRestaurantId(day, restId);
    }
}