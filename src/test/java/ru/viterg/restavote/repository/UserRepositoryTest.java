package ru.viterg.restavote.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.viterg.restavote.model.Role;
import ru.viterg.restavote.model.User;
import ru.viterg.restavote.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.viterg.restavote.UserTestData.*;

public class UserRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    protected UserRepository userRepository;

    @Test
    void create() {
        User created = userRepository.save(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                userRepository.save(new User(null, "Duplicate", "user1@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    void delete() {
        userRepository.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> userRepository.get(USER_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> userRepository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        User user = userRepository.get(ADMIN_ID);
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> userRepository.get(NOT_FOUND));
    }

    @Test
    void getByEmail() {
        User user = userRepository.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    // @Test
    // void update() {
    //     User updated = getUpdated();
    //     userRepository.update(updated);
    //     USER_MATCHER.assertMatch(userRepository.get(USER_ID), getUpdated());
    // }

    @Test
    void getAll() {
        List<User> all = userRepository.getAll();
        USER_MATCHER.assertMatch(all, admin, user1, user2);
    }

    // @Test
    // void createWithException() {
    //     validateRootCause(() -> userRepository
    //             .create(new User(null, "  ", "mail@yandex.ru", "password", 2000, Role.USER)), ConstraintViolationException.class);
    //     validateRootCause(() -> userRepository.create(new User(null, "User", "  ", "password", 2000, Role.USER)), ConstraintViolationException.class);
    //     validateRootCause(() -> userRepository.create(new User(null, "User", "mail@yandex.ru", "  ", 2000, Role.USER)), ConstraintViolationException.class);
    //     validateRootCause(() -> userRepository
    //             .create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())), ConstraintViolationException.class);
    //     validateRootCause(() -> userRepository
    //             .create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())), ConstraintViolationException.class);
    // }
}