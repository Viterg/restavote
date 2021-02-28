package ru.viterg.restavote.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.Vote;
import ru.viterg.restavote.repository.*;
import ru.viterg.restavote.util.exception.IllegalRequestDataException;

import java.time.*;
import java.util.Optional;

import static ru.viterg.restavote.util.DateTimeUtil.isTimeAvailableForVote;
import static ru.viterg.restavote.util.ValidationUtil.*;

@Service
public class VoteService {

    private final VoteRepository       voteRepository;
    private final UserRepository       userRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository,
                       RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Vote create(int userId, int restId) {
        return save(new Vote(), userId, restId);
    }

    public void update(int userId, int restId, LocalDate day, LocalTime voteTime) {
        Vote vote1 = checkNotFoundWithId(voteRepository.getByUserIdAndVoteDate(userId, day), "Entity not found");
        if (isTimeAvailableForVote(voteTime)) {
            save(vote1, userId, restId);
        } else {
            throw new IllegalRequestDataException("Time to vote is already pass");
        }
    }

    @Transactional
    public Vote save(Vote vote, int userId, int restaurantId) {
        if (vote.getVoteDate() == null) vote.setVoteDate(LocalDate.now(Clock.systemDefaultZone()));
        vote.setUser(userRepository.getOne(userId));
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        return voteRepository.save(vote);
    }

    public Vote get(int userId, LocalDate day) {
        return checkNotFound(voteRepository.getByUserIdAndVoteDate(userId, day), "user.id=" + userId + " for today");
    }

    public boolean delete(int userId, LocalDate day, LocalTime voteTime) {
        Optional<Vote> oldVote = voteRepository.getByUserIdAndVoteDate(userId, day);
        if (oldVote.isPresent() && isTimeAvailableForVote(voteTime)) {
            return voteRepository.delete(userId, day) != 0;
        } else {
            throw new IllegalRequestDataException("Vote cannot be delete now");
        }
    }
}