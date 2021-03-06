package ru.dsoccer1980.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.dsoccer1980.domain.Dish;
import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.domain.Role;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.util.config.InitProps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DishRepositoryTest extends AbstractDataJpaTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final Role ROLE_COMPANY = new Role(40L, InitProps.ROLE_COMPANY);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_COMPANY));
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Set.of(ROLE_COMPANY));
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);
    private final Dish DISH1 = new Dish(20L, "Borsh", new BigDecimal(255.3).setScale(2, RoundingMode.FLOOR), RESTAURANT1, LocalDate.of(2019, 7, 24));
    private final Dish DISH2 = new Dish(21L, "Soljanka", new BigDecimal(235.3).setScale(2, RoundingMode.FLOOR), RESTAURANT2, LocalDate.of(2019, 7, 23));

    @Autowired
    private DishRepository dishRepository;

    @Test
    void getAll() {
        assertThat(dishRepository.findAll()).isEqualTo(Arrays.asList(DISH1, DISH2));
    }

    @Test
    void getById() {
        assertThat(dishRepository.findById(DISH1.getId()).orElse(null)).isEqualTo(DISH1);
    }

    @Test
    void create() {
        Dish newDish = dishRepository.save(new Dish("New dish", new BigDecimal(270.0), RESTAURANT1, LocalDate.of(2019, 7, 25)));
        assertThat(dishRepository.findAll()).isEqualTo(Arrays.asList(DISH1, DISH2, newDish));
    }

    @Test
    void update() {
        Dish dishForUpdate = dishRepository.findById(DISH1.getId()).orElse(null);
        dishForUpdate.setName("Dish new name");
        dishRepository.save(dishForUpdate);

        assertThat(dishRepository.findById(DISH1.getId()).orElse(null)).isEqualTo(dishForUpdate);
    }

    @Test
    void delete() {
        dishRepository.deleteById(DISH1.getId());
        assertThat(dishRepository.findAll()).isEqualTo(Collections.singletonList(DISH2));
    }

    @Test
    void deleteWithWrongId() {
        assertThrows(EmptyResultDataAccessException.class, () -> dishRepository.deleteById(-1L));
        assertThat(dishRepository.findAll()).isEqualTo(Arrays.asList(DISH1, DISH2));
    }

    @Test
    void getDishByDate() {
        List<Dish> dishByDate = dishRepository.findDishByDate(LocalDate.of(2019, 7, 24));
        assertThat(dishByDate).isEqualTo(Collections.singletonList(DISH1));
    }

    @Test
    void findDishByRestaurantIdAndDate() {
        List<Dish> result = dishRepository.findDishByRestaurantIdAndDate(RESTAURANT1.getId(), LocalDate.of(2019, 7, 24));
        assertThat(result).isEqualTo(Collections.singletonList(DISH1));
    }


}
