package ru.dsoccer1980.service;


import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.util.exception.NotFoundException;

public interface RestaurantService {

    Restaurant get(long id) throws NotFoundException;

    Restaurant create(Restaurant restaurant);

    Restaurant update(Restaurant restaurant);

    void delete(long id) throws NotFoundException;

    Restaurant getRestaurantByUserId(long userId);
}