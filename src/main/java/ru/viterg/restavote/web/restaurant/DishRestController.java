package ru.viterg.restavote.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.viterg.restavote.model.Dish;
import ru.viterg.restavote.service.DishService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.viterg.restavote.util.ValidationUtil.*;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {
    public static final  String REST_URL = "/restaurants/{restId}";
    private static final Logger log      = LoggerFactory.getLogger(DishRestController.class);

    private final DishService service;

    public DishRestController(DishService service) {
        this.service = service;
    }

    @GetMapping("/dishes/{id}")
    public Dish get(@PathVariable int id, @PathVariable int restId) {
        log.info("get dish {} for restaurant {}", id, restId);
        return service.get(id, restId);
    }

    @DeleteMapping("/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restId) {
        log.info("delete dish {} for restaurant {}", id, restId);
        checkNotFoundWithId(service.delete(id, restId), id);
    }

    @GetMapping("/dishes")
    public List<Dish> getAll(@PathVariable int restId) {
        log.info("getAll for restaurant {}", restId);
        return service.getAll(restId);
    }

    @PutMapping(value = "/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restId) {
        assureIdConsistent(dish, id);
        log.info("update {} for restaurant {}", dish, restId);
        Assert.notNull(dish, "dish must not be null");
        checkNotFoundWithId(service.save(dish, restId), dish.id());
    }

    @PostMapping(value = "/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restId) {
        Dish created = create(dish, restId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path("/restaurants/" + restId + "/{id}")
                                                          .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    public Dish create(Dish dish, int restId) {
        checkNew(dish);
        log.info("create {} for restaurant {}", dish, restId);
        Assert.notNull(dish, "dish must not be null");
        return service.save(dish, restId);
    }
}