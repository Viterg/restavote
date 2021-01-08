package ru.viterg.restavote.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.repository.DishRepository;
import ru.viterg.restavote.web.AbstractControllerTest;
import ru.viterg.restavote.web.json.JsonUtil;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.viterg.restavote.RestaurantsTestData.*;
import static ru.viterg.restavote.TestUtil.*;
import static ru.viterg.restavote.UserTestData.admin;
import static ru.viterg.restavote.util.exception.ErrorType.VALIDATION_ERROR;

class DishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/restaurants/{restId}/dishes/";

    @Autowired
    private DishRepository repository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_START_ID, RESTAURANT_START_ID).with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_START_ID, RESTAURANT_START_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND, RESTAURANT_START_ID).with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_START_ID, RESTAURANT_START_ID).with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        assertNull(repository.get(DISH_START_ID, RESTAURANT_START_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND, RESTAURANT_START_ID).with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdatedDish();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_START_ID, RESTAURANT_START_ID).contentType(
                MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updated))
                                      .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(repository.get(DISH_START_ID, RESTAURANT_START_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getNewDish();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_START_ID)
                                                             .contentType(MediaType.APPLICATION_JSON)
                                                             .content(JsonUtil.writeValue(newDish))
                                                             .with(userHttpBasic(admin)));
        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(repository.get(newId, RESTAURANT_START_ID), newDish);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, RESTAURANT_START_ID).with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(List.of(dish1, dish4)));
    }

    @Test
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, null, "Dummy", 200);
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_START_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid))
                                      .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH_START_ID, null, null, 6000);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_START_ID, RESTAURANT_START_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid))
                                      .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        LocalDate now = LocalDate.now(Clock.systemDefaultZone());
        Dish invalid = new Dish(DISH_START_ID, now, "<script>alert(123)</script>", 200);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_START_ID, RESTAURANT_START_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid))
                                      .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }
}