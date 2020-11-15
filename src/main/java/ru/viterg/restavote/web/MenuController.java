package ru.viterg.restavote.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Для админа "добавить блюдо" - форма добавления - получаем дату, описание, цену и ид ресторана; "Редактировать"/"Удалить")
 */
@Controller
@RequestMapping("/menuOfDay")
public class MenuController {

    @GetMapping
    public Map<String, Integer> getMenuOfDay(@RequestParam int restId) {
        return null;
    }

    @GetMapping("/create")
    public String create(Model model) {
        // Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        // model.addAttribute("dish", meal);
        return "dishForm";
    }

    @GetMapping("/update")
    public String update(@RequestParam int dishId) {
        // Meal meal = get(dishId);
        // request.setAttribute("meal", meal);
        return "dishForm";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int dishId) {
        // delete(dishId);
        return "redirect:/menu";
    }

    @PostMapping
    public String doPost(@RequestParam String dishId, @RequestParam String dateTime, @RequestParam String description) {
        // Meal meal = new Meal(LocalDateTime.parse(dateTime), description));
        // if (!StringUtils.hasText(dishId)) {
        //     create(meal);
        // } else {
        //     update(meal, Integer.parseInt(dishId));
        // }
        return "redirect:/menu";
    }

}