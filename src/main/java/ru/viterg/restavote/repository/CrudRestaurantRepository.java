package ru.viterg.restavote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.viterg.restavote.model.Restaurant;

public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
