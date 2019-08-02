package ru.dsoccer1980.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Dish;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.util.config.InitProps;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@AutoConfigureTestEntityManager
public class DishServiceImplTest extends AbstractServiceTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final Role ROLE_COMPANY = new Role(40L, InitProps.ROLE_COMPANY);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_COMPANY));
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Set.of(ROLE_COMPANY));
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);

    private final Dish DISH1 = new Dish(20L, "Borsh", new BigDecimal(255.3).setScale(2, RoundingMode.FLOOR), RESTAURANT1, LocalDate.of(2019, 7, 24));
    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    private DishService dishService;

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
        assertThat(dishService.get(newDish.getId())).isEqualTo(newDish);
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
        assertThrows(NotFoundException.class, () -> dishService.get(DISH1.getId()));
    }

    @Test
    void deleteWithWrongId() {
        assertThrows(NotFoundException.class, () -> dishService.delete(-1));
    }

    @Test
    void findDishByRestaurantIdAndDate() {
        List<Dish> result = dishService.getDishByRestaurantAndDate(RESTAURANT1.getId(), LocalDate.of(2019, 7, 24));
        assertThat(result).isEqualTo(Collections.singletonList(DISH1));
    }

    @Test
    void getRestaurantsIntroducedTodayMenu() {
        testEntityManager.persist(new Dish("Today dish", new BigDecimal(235.3).setScale(2, RoundingMode.FLOOR), RESTAURANT2, LocalDate.now()));
        Set<Restaurant> restaurants = dishService.getRestaurantsIntroducedTodayMenu();
        assertThat(restaurants).isEqualTo(Set.of(RESTAURANT2));
    }

    @Test
    void getDatesByRestaurant() {
        List<LocalDate> datesByRestaurant = dishService.getDatesByRestaurant(RESTAURANT1.getId());
        assertThat(datesByRestaurant).isEqualTo(Collections.singletonList(LocalDate.of(2019, 7, 24)));
    }


}
