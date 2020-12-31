package ru.viterg.restavote.web;

import org.springframework.beans.factory.annotation.Autowired;
import ru.viterg.restavote.repository.DishRepository;
import ru.viterg.restavote.repository.RestaurantRepository;
import ru.viterg.restavote.web.restaurant.DishRestController;
import ru.viterg.restavote.web.restaurant.RestaurantRestController;

public class RestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantRestController.REST_URL + '/';

    @Autowired
    private RestaurantRepository repository;
}
