package ru.viterg.restavote.web.restaurant;

import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.model.Restaurant;
import ru.viterg.restavote.repository.DishRepository;
import ru.viterg.restavote.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.viterg.restavote.util.ValidationUtil.*;

/**
 * Исходим из того, что для админа появляются дополнительные кнопки на тех же страницах, что и у юзера. Полный список
 * ресторанов для просмотра (для админа "добавить" - форма добавления - получаем название ресторана). У каждого
 * ресторана ссылка на его меню дня со списком блюд, который может быть пустым. Проголосовать можно как на странице со
 * списком ресторанов, так и в меню дня - проверка будет после отправки.
 */
@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {

    public static final  String REST_URL = "/restaurants";
    private static final Logger log      = getLogger(RestaurantRestController.class);

    private final RestaurantRepository restaurantRepository;
    private final DishRepository       dishRepository;

    public RestaurantRestController(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.getAll();
    }

    @GetMapping("/{restId}")
    public Restaurant get(@PathVariable int restId) {
        log.info("get {}", restId);
        return checkNotFoundWithId(restaurantRepository.get(restId), restId);
    }

    @DeleteMapping("/{restId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restId) {
        log.info("delete {}", restId);
        checkNotFoundWithId(restaurantRepository.delete(restId), restId);
    }

    @GetMapping("/{restId}/menuOfDay")
    public List<Dish> getMenuOfDay(@PathVariable int restId) {
        LocalDate day = LocalDate.now();
        log.info("getOfDay {} for user {}", day, restId);
        return dishRepository.getMenuOfDay(day, restId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> add(@Valid @RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        log.info("add restaurant with name {}", restaurant.getDescription());
        Assert.notNull(restaurant, "restaurant must not be null");
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(REST_URL + "/{id}")
                                                          .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int restId) {
        assureIdConsistent(restaurant, restId);
        log.info("update {} for user {}", restaurant, restId);
        Assert.notNull(restaurant, "dish must not be null");
        checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.id());
    }
}