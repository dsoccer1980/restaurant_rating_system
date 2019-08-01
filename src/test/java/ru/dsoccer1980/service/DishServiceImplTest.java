package ru.dsoccer1980.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Dish;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
public class DishServiceImplTest extends AbstractServiceTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(Role.COMPANY));
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Set.of(Role.COMPANY));
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);

    private final Dish DISH1 = new Dish(20L, "Borsh", new BigDecimal(255.3).setScale(2, RoundingMode.FLOOR), RESTAURANT1, LocalDate.of(2019, 7, 24));
    private final Dish DISH2 = new Dish(21L, "Soljanka", new BigDecimal(235.3).setScale(2, RoundingMode.FLOOR), RESTAURANT2, LocalDate.of(2019, 7, 23));

    @Autowired
    private DishService dishService;

    @Test
    void getAll() {
        assertThat(dishService.getAll()).isEqualTo(Arrays.asList(DISH1, DISH2));
    }

    @Test
    void getById() {
        assertThat(dishService.get(DISH1.getId())).isEqualTo(DISH1);
    }

    @Test
    void getNotExist() {
        assertThrows(NotFoundException.class, () -> dishService.get(-1));
    }

    @Test
    void create() {
        Dish newDish = dishService.create(new Dish("New dish", new BigDecimal(270.0), RESTAURANT1, LocalDate.of(2019, 7, 25)));
        assertThat(dishService.getAll()).isEqualTo(Arrays.asList(DISH1, DISH2, newDish));
    }

    @Test
    void update() {
        Dish dishForUpdate = dishService.get(DISH1.getId());
        dishForUpdate.setName("Dish new name");
        dishService.update(dishForUpdate);

        assertThat(dishService.get(DISH1.getId())).isEqualTo(dishForUpdate);
    }

    @Test
    void delete() {
        dishService.delete(DISH1.getId());
        assertThat(dishService.getAll()).isEqualTo(Collections.singletonList(DISH2));
    }

    @Test
    void deleteWithWrongId() {
        assertThrows(NotFoundException.class, () -> dishService.delete(-1));
    }

    @Test
    void getDishByDate() {
        Map<Restaurant, List<Dish>> result = dishService.getDishByDate(LocalDate.of(2019, 7, 24));
        assertThat(result).isEqualTo(Map.of(RESTAURANT1, Collections.singletonList(DISH1)));
    }

    @Test
    void findDishByRestaurantIdAndDate() {
        List<Dish> result = dishService.getDishByRestaurantAndDate(RESTAURANT1.getId(), LocalDate.of(2019, 7, 24));
        assertThat(result).isEqualTo(Collections.singletonList(DISH1));
    }


}
