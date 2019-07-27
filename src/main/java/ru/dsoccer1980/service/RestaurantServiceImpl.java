package ru.dsoccer1980.service;

import org.springframework.stereotype.Service;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.util.List;
import java.util.Objects;

import static ru.dsoccer1980.util.ValidationUtil.checkNotFoundWithId;


@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantServiceImpl(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant get(long id) throws NotFoundException {
        Restaurant restaurant = repository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id:" + id));
        return checkNotFoundWithId(restaurant, id);
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
    public Restaurant update(Restaurant restaurant) throws NotFoundException {
        Objects.requireNonNull(restaurant, "restaurant must not be null");
        return checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }

    @Override
    public void delete(long id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

}