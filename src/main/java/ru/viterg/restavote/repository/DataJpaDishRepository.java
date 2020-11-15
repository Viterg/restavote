package ru.viterg.restavote.repository;

import org.springframework.stereotype.Repository;

@Repository
public class DataJpaDishRepository {
    private final CrudDishRepository repository;

    public DataJpaDishRepository(CrudDishRepository repository) {
        this.repository = repository;
    }
}