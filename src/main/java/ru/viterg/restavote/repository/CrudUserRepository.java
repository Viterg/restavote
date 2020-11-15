package ru.viterg.restavote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.viterg.restavote.model.User;

public interface CrudUserRepository extends JpaRepository<User, Integer> {
}
