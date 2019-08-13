package ru.dsoccer1980.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.dsoccer1980.domain.Dish;
import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.repository.DishRepository;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class DishServiceImpl implements DishService {

    private final DishRepository repository;

    public DishServiceImpl(DishRepository repository) {
        this.repository = repository;
    }

    @Override
    public Dish get(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id:" + id));
    }

    @Override
    public Dish create(Dish dish) {
        Objects.requireNonNull(dish, "dish must not be null");
        return repository.save(dish);
    }

    @Override
    public void delete(long dishId) throws NotFoundException {
        try {
            repository.deleteById(dishId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Not found entity with id:" + dishId);
        }
    }

    @Override
    public Dish update(Dish dish) {
        Objects.requireNonNull(dish, "dish must not be null");
        return repository.save(dish);
    }

    @Override
    public List<Dish> getDishByRestaurantAndDate(long id, LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return repository.findDishByRestaurantIdAndDate(id, date);
    }

    @Override
    public List<LocalDate> getDatesByRestaurant(long id) {
        return repository.findDatesByRestaurantId(id);
    }

    @Override
    public Set<Restaurant> getRestaurantsIntroducedTodayMenu() {
        return repository.findDishByDate(LocalDate.now())
                .stream()
                .map(Dish::getRestaurant)
                .collect(Collectors.toSet());
    }

}