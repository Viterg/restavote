package ru.viterg.restavote.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.id=?1 AND d.restaurant.id=?2")
    int delete(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.id=?1 AND d.restaurant.id=?2")
    Optional<Dish> get(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = ?1")
    List<Dish> getAllByRestaurantId(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.day = ?1 AND d.restaurant.id = ?2")
    List<Dish> getAllByDayAndRestaurantId(LocalDate day, int restaurantId);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant r WHERE d.day = ?1")
    List<Dish> getAllByDay(LocalDate day);
}