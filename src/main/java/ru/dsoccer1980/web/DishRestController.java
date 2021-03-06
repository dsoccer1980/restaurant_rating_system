package ru.dsoccer1980.web;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dsoccer1980.domain.Dish;
import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.security.AuthorizedUser;
import ru.dsoccer1980.service.DishService;
import ru.dsoccer1980.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@RestController
public class DishRestController {

    private final DishService dishService;
    private final RestaurantService restaurantService;

    public DishRestController(DishService dishService, RestaurantService restaurantService) {
        this.dishService = dishService;
        this.restaurantService = restaurantService;
    }

    @PostMapping(value = "/company/dish")
    public Dish createDish(@RequestBody Dish dish) {
        if (dish.getDate().isBefore(LocalDate.now())) {
            return null;
        }
        dish.setRestaurant(restaurantService.get(getRestaurantId()));
        return dishService.create(dish);
    }

    @DeleteMapping(value = "/company/dish/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteDishById(@PathVariable("id") long id) {
        dishService.delete(id);
    }

    @PutMapping(value = "/company/dish")
    public void update(@RequestBody Dish dish) {
        dish.setRestaurant(dishService.get(dish.getId()).getRestaurant());
        dishService.update(dish);
    }

    @GetMapping(value = "/company/dish/date")
    public List<LocalDate> getDatesByRestaurant() {
        return dishService.getDatesByRestaurant(getRestaurantId());
    }

    @GetMapping(value = "/company/dish/date/{date}")
    public List<Dish> getDishByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dishService.getDishByRestaurantAndDate(getRestaurantId(), date);
    }

    @GetMapping("/company/dish/{id}")
    public Dish getDishById(@PathVariable("id") long id) {
        return dishService.get(id);
    }

    @GetMapping(value = "/user/dish/restaurant/{id}/date/{date}")
    public List<Dish> getDishByRestaurantAndDate(@PathVariable("id") long id,
                                                 @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return dishService.getDishByRestaurantAndDate(id, date);
    }

    @GetMapping(value = "/user/restaurant")
    public Set<Restaurant> getRestaurantsIntroducedTodayMenu() {
        return dishService.getRestaurantsIntroducedTodayMenu();
    }

    private long getRestaurantId() {
        long userId = AuthorizedUser.get().getId();
        return restaurantService.getRestaurantByUserId(userId).orElse(null).getId();
    }

}