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
import ru.dsoccer1980.model.Vote;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan({"ru.dsoccer1980.repository"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VoteRepositoryTest {


    private final User USER1 = new User("Ivanov", "ivan@gmail.com", "password", Role.USER);
    private final User USER2 = new User("Petrov", "petr@gmail.com", "password2", Role.USER);
    private final Restaurant RESTAURANT1 = new Restaurant("TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant("Europe", "Mihailovskaja 14", USER2);
    private final Vote VOTE1 = new Vote(USER1, RESTAURANT1, LocalDate.of(2019, 7, 24));
    private final Vote VOTE2 = new Vote(USER2, RESTAURANT1, LocalDate.of(2019, 7, 24));
    private final Vote VOTE3 = new Vote(USER2, RESTAURANT2, LocalDate.of(2019, 7, 25));

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    void beforeEach() {
        restaurantRepository.deleteAll();
        restaurantRepository.save(RESTAURANT1);
        restaurantRepository.save(RESTAURANT2);
        userRepository.deleteAll();
        userRepository.save(USER1);
        userRepository.save(USER2);
        voteRepository.deleteAll();
        voteRepository.save(VOTE1);
        voteRepository.save(VOTE2);
        voteRepository.save(VOTE3);
    }

    @Test
    void getAll() {
        assertThat(voteRepository.findAll()).isEqualTo(Arrays.asList(VOTE1, VOTE2, VOTE3));
    }

    @Test
    void getById() {
        assertThat(voteRepository.findById(VOTE1.getId()).orElse(null)).isEqualTo(VOTE1);
    }

    @Test
    void create() {
        Vote newVote = voteRepository.save(new Vote(USER1, RESTAURANT2, LocalDate.of(2019, 7, 26)));
        assertThat(voteRepository.findAll()).isEqualTo(Arrays.asList(VOTE1, VOTE2, VOTE3, newVote));
    }

    @Test
    void update() {
        Vote userForUpdate = voteRepository.findById(VOTE1.getId()).orElse(null);
        userForUpdate.setRestaurant(RESTAURANT2);
        voteRepository.save(userForUpdate);

        assertThat(voteRepository.findById(VOTE1.getId()).orElse(null)).isEqualTo(userForUpdate);
    }

    @Test
    void delete() {
        voteRepository.delete(VOTE1);
        assertThat(voteRepository.findAll()).isEqualTo(Arrays.asList(VOTE2, VOTE3));
    }


}
