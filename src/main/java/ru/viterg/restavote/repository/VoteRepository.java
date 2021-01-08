package ru.viterg.restavote.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.*;
import ru.viterg.restavote.util.DateTimeUtil;
import ru.viterg.restavote.util.exception.IllegalRequestDataException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class VoteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final CrudVoteRepository repository;
    private       LocalTime          voteTime;

    public VoteRepository(CrudVoteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Vote save(Vote vote, int userId, int restaurantId) {
        LocalTime votingTime = voteTime == null ? LocalTime.now() : voteTime;
        if (votingTime.isBefore(DateTimeUtil.LAST_VOTE_TIME)) {
            if (vote.getVoteDate() == null) vote.setVoteDate(LocalDate.now());
            vote.setUser(entityManager.getReference(User.class, userId));
            vote.setRestaurant(entityManager.getReference(Restaurant.class, restaurantId));
            return repository.save(vote);
        } else {
            throw new IllegalRequestDataException("Time to vote is already pass");
        }
    }

    @Transactional
    public boolean delete(int userId, LocalDate day) {
        return repository.delete(userId, day) != 0;
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    public Vote get(int userId, LocalDate day) {
        return repository.getByUserIdAndVoteDate(userId, day);
    }

    public List<Vote> getAll(int restaurantId, LocalDate day) {
        return repository.getAllByRestaurantIdAndVoteDate(restaurantId, day);
    }

    public void setVoteTime(LocalTime voteTime) {
        this.voteTime = voteTime;
    }
}
