package ru.dsoccer1980.web;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dsoccer1980.model.Dish;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.service.DishService;
import ru.dsoccer1980.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DishAdminRestController {

    private final DishService dishService;
    private final RestaurantService restaurantService;

    public DishAdminRestController(DishService dishService, RestaurantService restaurantService) {
        this.dishService = dishService;
        this.restaurantService = restaurantService;
    }

    @PostMapping(value = "/admin/dish", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Dish createDish(@RequestBody Dish dish) {
        if (dish.getDate().isBefore(LocalDate.now())) {
            return null;
        }
        dish.setRestaurant(restaurantService.get(getRestaurantId()));
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

    @GetMapping(value = "/admin/dish/date")
    public Set<LocalDate> getDatesByRestaurant() {
        return dishService.getDatesByRestaurant(getRestaurantId());
    }

    @GetMapping(value = "/admin/dish/date/{date}")
    public List<Dish> getDishByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dishService.getDishByRestaurantAndDate(getRestaurantId(), date);
    }

    @GetMapping(value = "/dish/restaurant/{id}/date/{date}")
    public List<Dish> getDishByRestaurantAndDate(@PathVariable("id") long id,
                                                 @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dishService.getDishByRestaurantAndDate(id, date);
    }

    @GetMapping("/dish/{id}")
    public Dish getDishById(@PathVariable("id") long id) {
        return dishService.get(id);
    }

    @GetMapping(value = "/user/restaurant")
    public Set<Restaurant> getRestaurantsIntroducedTodayMenu() {
        return dishService.getRestaurantsIntroducedTodayMenu();
    }

    private long getRestaurantId() {
        long userId = AuthorizedUser.get().getId();
        return restaurantService.getRestaurantByUserId(userId).getId();
    }

}