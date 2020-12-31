package ru.viterg.restavote.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.repository.DishRepository;
import ru.viterg.restavote.util.exception.NotFoundException;
import ru.viterg.restavote.web.restaurant.DishRestController;
import ru.viterg.restavote.web.json.JsonUtil;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.viterg.restavote.RestaurantsTestData.*;
import static ru.viterg.restavote.TestUtil.*;
import static ru.viterg.restavote.UserTestData.admin;
import static ru.viterg.restavote.util.DishesUtil.*;
import static ru.viterg.restavote.util.exception.ErrorType.VALIDATION_ERROR;
import static ru.viterg.restavote.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_DATETIME;

class DishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DishRestController.REST_URL + "103/dishes/";

    @Autowired
    private DishRepository repository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_START_ID).with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dish1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_START_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_START_ID).with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_START_ID).with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> repository.get(DISH_START_ID, RESTAURANT_START_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND).with(userHttpBasic(admin)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdatedDish();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_START_ID).contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updated))
                                      .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(repository.get(DISH_START_ID, RESTAURANT_START_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        Dish newMeal = getNewDish();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                                                             .contentType(MediaType.APPLICATION_JSON)
                                                             .content(JsonUtil.writeValue(newMeal))
                                                             .with(userHttpBasic(admin)));
        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newMeal.setId(newId);
        DISH_MATCHER.assertMatch(created, newMeal);
        DISH_MATCHER.assertMatch(repository.get(newId, RESTAURANT_START_ID), newMeal);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL).with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(getTos(List.of(dish1, dish4))));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                                      .param("startDate", "2020-01-30").param("startTime", "07:00")
                                      .param("endDate", "2020-01-31").param("endTime", "11:00")
                                      .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(DISH_TO_MATCHER.contentJson(createTo(dish5), createTo(dish1)));
    }

    @Test
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, null, "Dummy", 200);
        perform(MockMvcRequestBuilders.post(REST_URL)
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
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_START_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid))
                                      .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        Dish invalid = new Dish(DISH_START_ID, LocalDate.now(Clock.systemDefaultZone()), "<script>alert(123)</script>", 200);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_START_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid))
                                      .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Dish invalid = new Dish(DISH_START_ID, dish1.getDay(), "Dummy", 200);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_START_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid))
                                      .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_DATETIME));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Dish invalid = new Dish(null, dish1.getDay(), "Dummy", 200);
        perform(MockMvcRequestBuilders.post(REST_URL)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid))
                                      .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_DATETIME));
    }
}