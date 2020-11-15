package ru.viterg.restavote.repository;

import org.springframework.stereotype.Repository;

@Repository
public class DataJpaRestaurantRepository {
    private final CrudRestaurantRepository repository;

    public DataJpaRestaurantRepository(CrudRestaurantRepository repository) {
        this.repository = repository;
    }
}