package ru.dsoccer1980.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.service.RestaurantService;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class RestaurantServiceImplTest {

    private final Restaurant RESTAURANT1 = new Restaurant("TSAR", "Nevskij 53");
    private final Restaurant RESTAURANT2 = new Restaurant("Europe", "Mihailovskaja 14");

    @Autowired
    private RestaurantRepository repository;
    @Autowired
    private RestaurantService service;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
        repository.save(RESTAURANT1);
        repository.save(RESTAURANT2);
    }

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
    void create() {
        Restaurant newRestaurant = service.create(new Restaurant("New", "Address"));
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
}