package ru.dsoccer1980.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Dish;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.repository.DishRepository;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DishServiceImplTest {

    private final Restaurant RESTAURANT1 = new Restaurant("TSAR", "Nevskij 53");
    private final Restaurant RESTAURANT2 = new Restaurant("Europe", "Mihailovskaja 14");

    private final Dish DISH1 = new Dish("Borsh", 2553, RESTAURANT1, LocalDate.of(2019, 7, 24));
    private final Dish DISH2 = new Dish("Soljanka", 2353, RESTAURANT2, LocalDate.of(2019, 7, 23));

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DishService dishService;

    @BeforeEach
    void beforeEach() {
        restaurantRepository.deleteAll();
        restaurantRepository.save(RESTAURANT1);
        restaurantRepository.save(RESTAURANT2);
        dishRepository.deleteAll();
        dishRepository.save(DISH1);
        dishRepository.save(DISH2);
    }

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
        Dish newDish = dishService.create(new Dish("New dish", 2700, RESTAURANT1, LocalDate.of(2019, 7, 25)));
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
    void getDishByDate() {
        Map<Restaurant, List<Dish>> result = dishService.getDishByDate(LocalDate.of(2019, 7, 24));
        assertThat(result).isEqualTo(Map.of(RESTAURANT1, Arrays.asList(DISH1)));
    }

    @Test
    void findDishByRestaurantIdAndDate() {
        List<Dish> result = dishService.getDishByRestaurantAndDate(RESTAURANT1.getId(), LocalDate.of(2019, 7, 24));
        assertThat(result).isEqualTo(Collections.singletonList(DISH1));
    }


}
