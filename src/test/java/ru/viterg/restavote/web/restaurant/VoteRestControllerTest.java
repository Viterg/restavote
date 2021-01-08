package ru.viterg.restavote.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.viterg.restavote.model.Vote;
import ru.viterg.restavote.repository.VoteRepository;
import ru.viterg.restavote.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.viterg.restavote.RestaurantsTestData.*;
import static ru.viterg.restavote.TestUtil.userHttpBasic;
import static ru.viterg.restavote.UserTestData.*;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/restaurants/{restId}/vote/";

    @Autowired
    private VoteRepository repository;

    @Test
    void makeVote() throws Exception {
        repository.setVoteTime(LocalTime.of(10, 0));
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_START_ID)
                                      .with(userHttpBasic(user1)))
                .andExpect(status().isNoContent());
        Vote actual = repository.get(user1.getId(), LocalDate.now());
        Vote expected = getNewVote();
        VOTE_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void updateVote() throws Exception {
        repository.setVoteTime(LocalTime.of(10, 0));
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_START_ID + 1)
                                      .with(userHttpBasic(user1)))
                .andExpect(status().isNoContent());
        Vote actual = repository.get(user1.getId(), LocalDate.now());
        Vote expected = getUpdatedVote();
        VOTE_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void failUpdateVote() throws Exception {
        repository.setVoteTime(LocalTime.of(12, 0));
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_START_ID + 1)
                                      .with(userHttpBasic(user1)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteAll() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL, RESTAURANT_START_ID)
                                      .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        assertTrue(repository.getAll(RESTAURANT_START_ID, LocalDate.now()).isEmpty());
    }
}