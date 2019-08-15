package ru.dsoccer1980.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.domain.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VoteRepositoryTest extends AbstractDataJpaTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final LocalDate VOTE_DATE1 = LocalDate.of(2019, 7, 24);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Collections.emptySet());
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Collections.emptySet());
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);


    private final Vote VOTE1 = new Vote(30L, USER1, RESTAURANT1, VOTE_DATE1);
    private final Vote VOTE2 = new Vote(31L, USER2, RESTAURANT1, VOTE_DATE1);
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
        int result = voteRepository.deleteByUserIdAndDate(USER1.getId(), VOTE1.getDate());
        assertTrue(result != 0);
        assertThat(voteRepository.findAll()).isEqualTo(Arrays.asList(VOTE2, VOTE3));
    }

    @Test
    void deleteWithWrongData() {
        int result = voteRepository.deleteByUserIdAndDate(-1, LocalDate.now());
        assertEquals(0, result);
    }

    @Test
    void findByUserIdAndDate() {
        Vote vote = voteRepository.findByUserIdAndDate(USER1.getId(), VOTE_DATE1).get();
        assertThat(vote).isEqualTo(VOTE1);
    }

    @Test
    void findByRestaurantIdAndDate() {
        List<Vote> votes = voteRepository.findByRestaurantIdAndDate(RESTAURANT1.getId(), VOTE_DATE1);
        assertThat(votes).isEqualTo(List.of(VOTE1, VOTE2));
    }

    @Test
    void findByUserId() {
        List<Vote> votes = voteRepository.findByUserId(USER2.getId());
        assertThat(votes).isEqualTo(List.of(VOTE2, VOTE3));
    }

    @Test
    void findByDate() {
        List<Vote> votes = voteRepository.findByDate(VOTE_DATE1);
        assertThat(votes).isEqualTo(List.of(VOTE1, VOTE2));
    }


}
