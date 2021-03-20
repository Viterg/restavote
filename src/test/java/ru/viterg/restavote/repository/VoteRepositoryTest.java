package ru.viterg.restavote.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.viterg.restavote.AbstractDataManageTest;
import ru.viterg.restavote.model.Vote;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.viterg.restavote.RestaurantsTestData.*;
import static ru.viterg.restavote.UserTestData.user1;

class VoteRepositoryTest extends AbstractDataManageTest {

    @Autowired
    protected VoteRepository repository;

    @Test
    void create() {
        Vote created = repository.save(getNewVote());
        int newId = created.id();
        Vote newVote = getNewVote();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.getByUserIdAndVoteDate(user1.getId(), newVote.getVoteDate()).orElse(null),
                                 newVote);
    }

    @Test
    void duplicateVoteCreate() {
        Vote vote = new Vote(null, user1, vote1.getVoteDate(), restaurant3);
        assertThrows(DataAccessException.class, () -> repository.save(vote));
    }

    // @Test
    // void wrongTimeVoteCreate() {
    //     assertThrows(IllegalRequestDataException.class, () -> repository.save(getNewVote()));
    // }

    @Test
    void update() {
        Vote updated = getUpdatedVote();
        repository.save(updated);
        Vote vote = repository.getByUserIdAndVoteDate(user1.getId(), updated.getVoteDate()).orElse(null);
        VOTE_MATCHER.assertMatch(vote, getUpdatedVote());
    }

    @Test
    void get() {
        VOTE_MATCHER.assertMatch(repository.getByUserIdAndVoteDate(user1.getId(), vote1.getVoteDate()).orElse(null), vote1);
    }

    @Test
    void delete() {
        repository.delete(user1.getId(), vote1.getVoteDate());
        assertTrue(repository.getByUserIdAndVoteDate(user1.getId(), vote1.getVoteDate()).isEmpty());
    }

    @Test
    void deleteNotFound() {
        assertEquals(0 , repository.delete(NOT_FOUND, vote1.getVoteDate()));
    }

    @Test
    void getNotFound() {
        assertTrue(repository.getByUserIdAndVoteDate(NOT_FOUND, vote1.getVoteDate()).isEmpty());
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> {
            repository.save(new Vote(null, null, vote1.getVoteDate(), restaurant1));
        });
        validateRootCause(ConstraintViolationException.class, () -> {
            repository.save(new Vote(null, user1, vote1.getVoteDate(), null));
        });
        validateRootCause(ConstraintViolationException.class, () -> {
            repository.save(new Vote(null, null, vote1.getVoteDate(), null));
        });
    }
}