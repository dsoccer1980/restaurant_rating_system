package ru.dsoccer1980.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan({"ru.dsoccer1980.repository"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RestaurantRepositoryTest {

    private final User USER1 = new User("Ivanov", "ivan@gmail.com", "password", Role.COMPANY);
    private final User USER2 = new User("Petrov", "petr@gmail.com", "password2", Role.COMPANY);
    private final User NEW_USER = new User("new", "new@gmail.com", "password3", Role.COMPANY);

    private final Restaurant RESTAURANT1 = new Restaurant("TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant("Europe", "Mihailovskaja 14", USER2);
    @Autowired
    private RestaurantRepository repository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        userRepository.save(USER1);
        userRepository.save(USER2);
        repository.deleteAll();
        repository.save(RESTAURANT1);
        repository.save(RESTAURANT2);
    }

    @Test
    void getAll() {
        assertThat(repository.findAll()).isEqualTo(Arrays.asList(RESTAURANT1, RESTAURANT2));
    }

    @Test
    void getById() {
        assertThat(repository.findById(RESTAURANT1.getId()).orElse(null)).isEqualTo(RESTAURANT1);
    }

    @Test
    void create() {
        userRepository.save(NEW_USER);
        Restaurant newRestaurant = repository.save(new Restaurant("New", "Address", NEW_USER));
        assertThat(repository.findAll()).isEqualTo(Arrays.asList(RESTAURANT1, RESTAURANT2, newRestaurant));
    }

    @Test
    void update() {
        Restaurant restaurantForUpdate = repository.findById(RESTAURANT1.getId()).orElse(null);
        restaurantForUpdate.setAddress("Update address");
        repository.save(restaurantForUpdate);

        assertThat(repository.findById(RESTAURANT1.getId()).orElse(null)).isEqualTo(restaurantForUpdate);
    }

    @Test
    void delete() {
        repository.delete(RESTAURANT1.getId());
        assertThat(repository.findAll()).isEqualTo(Collections.singletonList(RESTAURANT2));
    }

    @Test
    void deleteWithWrongId() {
        assertThat(repository.delete(-1)).isEqualTo(0);
        assertThat(repository.findAll()).isEqualTo(Arrays.asList(RESTAURANT1, RESTAURANT2));
    }


}
