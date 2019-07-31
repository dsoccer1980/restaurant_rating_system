package ru.dsoccer1980.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import ru.dsoccer1980.model.Dish;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan({"ru.dsoccer1980.repository"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DishRepositoryTest {

    private final User USER1 = new User("Ivanov", "ivan@gmail.com", "password", Role.COMPANY);
    private final User USER2 = new User("Petrov", "petr@gmail.com", "password2", Role.COMPANY);
    private final Restaurant RESTAURANT1 = new Restaurant("TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant("Europe", "Mihailovskaja 14", USER2);

    private final Dish DISH1 = new Dish("Borsh", new BigDecimal(255.3), RESTAURANT1, LocalDate.of(2019, 7, 24));
    private final Dish DISH2 = new Dish("Soljanka", new BigDecimal(235.3), RESTAURANT2, LocalDate.of(2019, 7, 23));

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        userRepository.save(USER1);
        userRepository.save(USER2);
        restaurantRepository.deleteAll();
        restaurantRepository.save(RESTAURANT1);
        restaurantRepository.save(RESTAURANT2);
        dishRepository.deleteAll();
        dishRepository.save(DISH1);
        dishRepository.save(DISH2);
    }

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
        dishRepository.delete(DISH1.getId());
        assertThat(dishRepository.findAll()).isEqualTo(Collections.singletonList(DISH2));
    }

    @Test
    void deleteWithWrongId() {
        assertThat(dishRepository.delete(-1)).isEqualTo(0);
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
