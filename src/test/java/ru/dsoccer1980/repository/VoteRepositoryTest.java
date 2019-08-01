package ru.dsoccer1980.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.model.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class VoteRepositoryTest extends AbstractDataJpaTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Collections.emptySet());
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Collections.emptySet());
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);


    private final Vote VOTE1 = new Vote(30L, USER1, RESTAURANT1, LocalDate.of(2019, 7, 24));
    private final Vote VOTE2 = new Vote(31L, USER2, RESTAURANT1, LocalDate.of(2019, 7, 24));
    private final Vote VOTE3 = new Vote(32L, USER2, RESTAURANT2, LocalDate.of(2019, 7, 25));

    @Autowired
    private VoteRepository voteRepository;

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
