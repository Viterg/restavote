package ru.viterg.restavote.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.User;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=?1")
    int delete(int id);

    User getByEmail(String email);

}