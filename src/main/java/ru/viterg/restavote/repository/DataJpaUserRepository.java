package ru.viterg.restavote.repository;

import org.springframework.stereotype.Repository;

@Repository
public class DataJpaUserRepository {
    private final CrudUserRepository repository;

    public DataJpaUserRepository(CrudUserRepository repository) {
        this.repository = repository;
    }
}
