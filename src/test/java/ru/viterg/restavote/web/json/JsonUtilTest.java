package ru.viterg.restavote.web.json;

import org.junit.jupiter.api.Test;
import ru.viterg.restavote.UserTestData;
import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.viterg.restavote.RestaurantsTestData.*;

class JsonUtilTest {

    @Test
    void readWriteValue() {
        String json = JsonUtil.writeValue(dish1);
        System.out.println(json);
        Dish meal = JsonUtil.readValue(json, Dish.class);
        DISH_MATCHER.assertMatch(meal, dish1);
    }

    @Test
    void readWriteValues() {
        String json = JsonUtil.writeValue(dishes);
        System.out.println(json);
        List<Dish> meals = JsonUtil.readValues(json, Dish.class);
        DISH_MATCHER.assertMatch(meals, dishes);
    }

    @Test
    void writeOnlyAccess() {
        String json = JsonUtil.writeValue(UserTestData.user1);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        String jsonWithPass = UserTestData.jsonWithPassword(UserTestData.user1, "newPass");
        System.out.println(jsonWithPass);
        User user1 = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(user1.getPassword(), "newPass");
    }
}