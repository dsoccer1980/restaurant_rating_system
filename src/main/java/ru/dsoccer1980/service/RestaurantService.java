package ru.dsoccer1980.service;


import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.util.Optional;

public interface RestaurantService {

    Restaurant get(long id) throws NotFoundException;

    Restaurant create(Restaurant restaurant);

    Restaurant update(Restaurant restaurant);

    void delete(long id) throws NotFoundException;

    Optional<Restaurant> getRestaurantByUserId(long userId);
}