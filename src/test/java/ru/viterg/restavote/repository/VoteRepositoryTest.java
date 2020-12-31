package ru.viterg.restavote.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.viterg.restavote.model.Vote;
import ru.viterg.restavote.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.viterg.restavote.RestaurantsTestData.*;
import static ru.viterg.restavote.UserTestData.user1;

class VoteRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    protected VoteRepository repository;

    @Test
    void delete() {
        repository.delete(user1.getId(), vote1.getVoteDate());
        assertThrows(NotFoundException.class, () -> repository.get(user1.getId(), vote1.getVoteDate()));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> repository.delete(NOT_FOUND, vote1.getVoteDate()));
    }

    @Test
    void create() {
        Vote created = repository.save(getNewVote(), user1.getId(), restaurant1.getId());
        int newId = created.id();
        Vote newVote = getNewVote();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.get(user1.getId(), newVote.getVoteDate()), newVote);
    }

    @Test
    void duplicateVoteCreate() {
        assertThrows(DataAccessException.class, () -> repository.save(
                new Vote(null, user1, vote1.getVoteDate(), restaurant3), user1.getId(), restaurant3.getId()));
    }

    @Test
    void get() {
        Vote actual = repository.get(user1.getId(), vote1.getVoteDate());
        VOTE_MATCHER.assertMatch(actual, vote1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> repository.get(NOT_FOUND, vote1.getVoteDate()));
    }

    @Test
    void update() {
        Vote updated = getUpdatedVote();
        repository.save(updated, user1.getId(), restaurant1.getId());
        VOTE_MATCHER.assertMatch(repository.get(user1.getId(), vote1.getVoteDate()), getUpdatedVote());
    }

    @Test
    void getAll() {
        VOTE_MATCHER.assertMatch(repository.getAll(restaurant1.getId(), vote1.getVoteDate()), vote1);
    }

    @Test
    void createWithException() {
        validateRootCause(() -> repository.save(new Vote(null, user1, null, restaurant1), 101, 105), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new Vote(null, null, vote1.getVoteDate(), restaurant1), 101, 105), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new Vote(null, user1, vote1.getVoteDate(), null), 101, 105), ConstraintViolationException.class);
        validateRootCause(() -> repository.save(new Vote(null, null, vote1.getVoteDate(), null), 101, 105), ConstraintViolationException.class);
    }
}