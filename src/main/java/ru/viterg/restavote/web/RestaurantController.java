package ru.viterg.restavote.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.viterg.restavote.model.Restaurant;
import ru.viterg.restavote.repository.CrudRestaurantRepository;

import java.util.List;
import java.util.Map;

/**
 * Исходим из того, что для админа появляются дополнительные кнопки на тех же страницах, что и у юзера. Полный список
 * ресторанов для просмотра (для админа "добавить" - форма добавления - получаем название ресторана). У каждого
 * ресторана ссылка на его меню дня со списком блюд, который может быть пустым. Проголосовать можно как на странице со
 * списком ресторанов, так и в меню дня - проверка будет после отправки.
 */
@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private CrudRestaurantRepository repository;

    public RestaurantController(CrudRestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<?> getAll() {
        return repository.findAll();
    }

    // @GetMapping
    // public String getAll(Model model) {
    //     model.addAttribute("restaurants", repository.getAll());
    //     return "restaurants";
    // }

    @GetMapping("/{id}/menuOfDay")
    public Map<String, Integer> getMenuOfDay(@PathVariable String id) {
        return null;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam String description) {
        Restaurant saved = repository.save(new Restaurant(description));
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PostMapping("/vote")
    public ResponseEntity<?> voteFor(@RequestParam int id) {
        // нужен userId для отслеживания и отмены прошлого голоса
        return new ResponseEntity<>(HttpStatus.OK);
    }
}