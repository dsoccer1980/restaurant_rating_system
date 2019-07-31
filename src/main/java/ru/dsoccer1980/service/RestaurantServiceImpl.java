package ru.dsoccer1980.service;

import org.springframework.stereotype.Service;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.util.List;
import java.util.Objects;


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
    public List<Restaurant> getAll() {
        return repository.findAll();
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
        if (repository.delete(id) == 0) {
            throw new NotFoundException("Not found entity with id:" + id);
        }
    }

    @Override
    public Restaurant getRestaurantByUserId(long userId) {
        return repository.findRestaurantByUserId(userId);
    }

}