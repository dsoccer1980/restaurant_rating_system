package ru.dsoccer1980.web;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dsoccer1980.model.Dish;
import ru.dsoccer1980.service.DishService;
import ru.dsoccer1980.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DishAdminRestController {

    private final DishService dishService;
    private final RestaurantService restaurantService;

    public DishAdminRestController(DishService dishService, RestaurantService restaurantService) {
        this.dishService = dishService;
        this.restaurantService = restaurantService;
    }

    @PostMapping(value = "/admin/dish/restaurant/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Dish createWithLocation(@RequestBody Dish dish, @PathVariable("restaurantId") long restaurantId) {
        dish.setRestaurant(restaurantService.get(restaurantId));
        return dishService.create(dish);
    }

    @DeleteMapping(value = "/admin/dish/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteDishById(@PathVariable("id") long id) {
        dishService.delete(id);
    }

    @PutMapping(value = "/admin/dish", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Dish dish) {
        dish.setRestaurant(dishService.get(dish.getId()).getRestaurant());
        dishService.update(dish);
    }

    @GetMapping(value = "/admin/dish/restaurant/{id}/date/{date}")
    public List<Dish> getDishByRestaurantAndDate(@PathVariable("id") long id,
                                                 @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dishService.getDishByRestaurantAndDate(id, date);
    }

    @GetMapping("/dish/{id}")
    public Dish getDishById(@PathVariable("id") long id) {
        return dishService.get(id);
    }

}