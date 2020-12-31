package ru.viterg.restavote.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudDishRepository extends JpaRepository<Dish, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.id=?1 AND d.restaurant.id=?2")
    int delete(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1")
    List<Dish> getAll(int restaurantId);
    List<Dish> getAllByRestaurantId(int restaurantId);

    @Query("SELECT d from Dish d WHERE d.restaurant.id=?1 AND d.day =?2")
    List<Dish> getMenuOfDay(LocalDate day, int restaurantId);
    List<Dish> getAllByDayAndRestaurantId(LocalDate day, int restaurantId);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.id = ?1 and d.restaurant.id = ?2")
    Dish getWithRestaurant(int id, int restaurantId);
}