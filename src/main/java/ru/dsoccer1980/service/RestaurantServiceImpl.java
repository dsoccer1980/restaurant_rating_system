package ru.dsoccer1980.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.util.Objects;
import java.util.Optional;


@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantServiceImpl(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant get(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id:" + id));
    }

    @Override
    public Restaurant create(Restaurant restaurant) {
        Objects.requireNonNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        Objects.requireNonNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Not found entity with id:" + id);
        }
    }

    @Override
    public Optional<Restaurant> getRestaurantByUserId(long userId) {
        return repository.findRestaurantByUserId(userId);
    }

}