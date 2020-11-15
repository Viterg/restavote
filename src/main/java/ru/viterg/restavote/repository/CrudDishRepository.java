package ru.viterg.restavote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.viterg.restavote.model.Dish;

public interface CrudDishRepository extends JpaRepository<Dish, Integer> {
}
