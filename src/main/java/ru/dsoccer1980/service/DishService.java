package ru.dsoccer1980.service;

import ru.dsoccer1980.model.Dish;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DishService {

    Dish get(long id) throws NotFoundException;

    List<Dish> getAll();

    Dish create(Dish dish);

    void delete(long dishId) throws NotFoundException;

    Dish update(Dish dish) throws NotFoundException;

    List<Dish> getDishByRestaurantAndDate(long id, LocalDate date);

    Map<Restaurant, List<Dish>> getDishByDate(LocalDate date);

    Set<LocalDate> getDatesByRestaurant(long id);

    Set<Restaurant> getRestaurantsIntroducedTodayMenu();
}