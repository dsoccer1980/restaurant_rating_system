package ru.dsoccer1980.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestaurantRepositoryTest extends AbstractDataJpaTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final Role ROLE_COMPANY = new Role(40L, "COMPANY");
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_COMPANY));
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Set.of(ROLE_COMPANY));

    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    private RestaurantRepository repository;


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
        User newUser = testEntityManager.persist(new User("new", "new@gmail.com", "password3", Set.of(ROLE_COMPANY)));
        Restaurant newRestaurant = repository.save(new Restaurant("New", "Address", newUser));
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
        repository.deleteById(RESTAURANT1.getId());
        assertThat(repository.findAll()).isEqualTo(Collections.singletonList(RESTAURANT2));
    }

    @Test
    void deleteWithWrongId() {
        assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(-1L));
        assertThat(repository.findAll()).isEqualTo(Arrays.asList(RESTAURANT1, RESTAURANT2));
    }


}
