package ru.viterg.restavote.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.viterg.restavote.model.Vote;
import ru.viterg.restavote.util.exception.IllegalRequestDataException;

import javax.validation.ConstraintViolationException;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.viterg.restavote.RestaurantsTestData.*;
import static ru.viterg.restavote.UserTestData.user1;

class VoteRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    protected VoteRepository repository;

    @Test
    void delete() {
        repository.delete(user1.getId(), vote1.getVoteDate());
        assertNull(repository.get(user1.getId(), vote1.getVoteDate()));
    }

    @Test
    void deleteNotFound() {
        assertFalse(repository.delete(NOT_FOUND, vote1.getVoteDate()));
    }

    @Test
    void create() {
        repository.setVoteTime(LocalTime.of(10, 0));
        Vote created = repository.save(getNewVote(), user1.getId(), restaurant1.getId());
        int newId = created.id();
        Vote newVote = getNewVote();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.get(user1.getId(), newVote.getVoteDate()), newVote);
    }

    @Test
    void duplicateVoteCreate() {
        repository.setVoteTime(LocalTime.of(10, 0));
        assertThrows(DataAccessException.class, () -> repository.save(
                new Vote(null, user1, vote1.getVoteDate(), restaurant3), user1.getId(), restaurant3.getId()));
    }

    @Test
    void wrongTimeVoteCreate() {
        repository.setVoteTime(null);
        assertThrows(IllegalRequestDataException.class,
                     () -> repository.save(getNewVote(), user1.getId(), restaurant1.getId()));
    }

    @Test
    void get() {
        Vote actual = repository.get(user1.getId(), vote1.getVoteDate());
        VOTE_MATCHER.assertMatch(actual, vote1);
    }

    @Test
    void getNotFound() {
        assertNull(repository.get(NOT_FOUND, vote1.getVoteDate()));
    }

    @Test
    void update() {
        Vote updated = getUpdatedVote();
        repository.setVoteTime(LocalTime.of(10, 0));
        repository.save(updated, user1.getId(), restaurant1.getId());
        VOTE_MATCHER.assertMatch(repository.get(user1.getId(), updated.getVoteDate()), getUpdatedVote());
    }

    @Test
    void getAll() {
        VOTE_MATCHER.assertMatch(repository.getAll(restaurant3.getId(), vote1.getVoteDate()), vote1);
    }

    @Test
    void createWithException() {
        repository.setVoteTime(LocalTime.of(10, 0));
        validateRootCause(() -> repository.save(new Vote(null, null, vote1.getVoteDate(), restaurant1), 101, 105), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new Vote(null, user1, vote1.getVoteDate(), null), 101, 105), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new Vote(null, null, vote1.getVoteDate(), null), 101, 105), ConstraintViolationException.class);
    }
}