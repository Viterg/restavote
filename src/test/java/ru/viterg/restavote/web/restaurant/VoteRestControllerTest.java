package ru.viterg.restavote.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.viterg.restavote.model.Vote;
import ru.viterg.restavote.service.VoteService;
import ru.viterg.restavote.util.DateTimeUtil;
import ru.viterg.restavote.util.exception.NotFoundException;
import ru.viterg.restavote.web.AbstractControllerTest;
import ru.viterg.restavote.web.json.JsonUtil;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.viterg.restavote.RestaurantsTestData.*;
import static ru.viterg.restavote.TestUtil.userHttpBasic;
import static ru.viterg.restavote.UserTestData.user1;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/restaurants/{restId}/vote/";

    @Autowired
    private VoteService service;

    @Test
    void makeVote() throws Exception {
        makeNewVote();
    }

    @Test
    void updateVote() throws Exception {
        if (DateTimeUtil.isTimeAvailableForVote(LocalTime.now(Clock.systemDefaultZone()))) {
            update(status().isNoContent());
            Vote actual = service.get(user1.getId(), LocalDate.now());
            Vote expected = getUpdatedVote();
            VOTE_MATCHER.assertMatch(actual, expected);
        } else {
            update(status().isUnprocessableEntity());
        }
    }

    private void update(ResultMatcher status) throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL + "{id}", RESTAURANT_START_ID + 1, DISH_START_ID + 6)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(getUpdatedVote()))
                                      .with(userHttpBasic(user1)))
                .andExpect(status);
    }

    @Test
    void getVote() throws Exception {
        Vote expected = makeNewVote();
        perform(MockMvcRequestBuilders.get(REST_URL + "{id}", RESTAURANT_START_ID + 1, DISH_START_ID + 6)
                                      .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(expected));
    }

    @Test
    void deleteVote() throws Exception {
        makeNewVote();
        if (DateTimeUtil.isTimeAvailableForVote(LocalTime.now(Clock.systemDefaultZone()))) {
            delete(status().isNoContent());
            assertThrows(NotFoundException.class, () -> service.get(user1.getId(), LocalDate.now()));
        } else {
            delete(status().isUnprocessableEntity());
        }
    }

    private void delete(ResultMatcher status) throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL, RESTAURANT_START_ID)
                                      .with(userHttpBasic(user1)))
                .andExpect(status);
    }

    private Vote makeNewVote() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_START_ID)
                                      .with(userHttpBasic(user1)))
                .andExpect(status().isCreated());
        Vote actual = service.get(user1.getId(), LocalDate.now());
        Vote expected = getNewVote();
        VOTE_MATCHER.assertMatch(actual, expected);
        return expected;
    }
}