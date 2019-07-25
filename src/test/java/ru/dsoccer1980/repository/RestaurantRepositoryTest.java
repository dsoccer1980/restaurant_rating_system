package ru.dsoccer1980.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.repository.RestaurantRepository;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan({"ru.dsoccer1980.repository"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RestaurantRepositoryTest {

    private final Restaurant RESTAURANT1 = new Restaurant("TSAR", "Nevskij 53");
    private final Restaurant RESTAURANT2 = new Restaurant("Europe", "Mihailovskaja 14");
    @Autowired
    private RestaurantRepository repository;

    @BeforeEach
    void beforeEach() {
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
        Restaurant newRestaurant = repository.save(new Restaurant("New", "Address"));
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


}
