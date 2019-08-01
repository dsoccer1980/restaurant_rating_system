package ru.dsoccer1980.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestEntityManager
class RestaurantServiceImplTest extends AbstractServiceTest {

    private final Role ROLE_COMPANY = new Role(40L, "COMPANY");
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password",
            LocalDateTime.of(2019, 7, 31, 0, 0, 0), Set.of(ROLE_COMPANY));
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2",
            LocalDateTime.of(2019, 7, 31, 0, 0, 0), Set.of(ROLE_COMPANY));

    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);

    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    private RestaurantService service;

    @Test
    void get() {
        assertThat(service.get(RESTAURANT1.getId())).isEqualTo(RESTAURANT1);
    }

    @Test
    void getNotExist() {
        assertThrows(NotFoundException.class, () -> service.get(-1));
    }

    @Test
    void getAll() {
        assertThat(service.getAll()).isEqualTo(Arrays.asList(RESTAURANT1, RESTAURANT2));
    }

    @Test
    @Transactional
    void create() {
        User newUser = testEntityManager.persist(new User("new", "new@gmail.com", "password3", Set.of(ROLE_COMPANY)));
        Restaurant newRestaurant = service.create(new Restaurant("New", "Address", newUser));
        assertThat(service.getAll()).isEqualTo(Arrays.asList(RESTAURANT1, RESTAURANT2, newRestaurant));
    }

    @Test
    void update() {
        Restaurant restaurantForUpdate = service.get(RESTAURANT1.getId());
        restaurantForUpdate.setAddress("Update address");
        service.update(restaurantForUpdate);

        assertThat(service.get(RESTAURANT1.getId())).isEqualTo(restaurantForUpdate);
    }

    @Test
    void delete() {
        service.delete(RESTAURANT1.getId());
        assertThat(service.getAll()).isEqualTo(Collections.singletonList(RESTAURANT2));
    }

    @Test
    void deleteWithWrongId() {
        assertThrows(NotFoundException.class, () -> service.delete(-1));
    }
}