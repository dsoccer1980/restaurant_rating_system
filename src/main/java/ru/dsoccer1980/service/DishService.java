package ru.dsoccer1980.service;

import ru.dsoccer1980.domain.Dish;
import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface DishService {

    Dish get(long id) throws NotFoundException;

    Dish create(Dish dish);

    void delete(long dishId) throws NotFoundException;

    Dish update(Dish dish) throws NotFoundException;

    List<Dish> getDishByRestaurantAndDate(long id, LocalDate date);

    List<LocalDate> getDatesByRestaurant(long id);

    Set<Restaurant> getRestaurantsIntroducedTodayMenu();
}