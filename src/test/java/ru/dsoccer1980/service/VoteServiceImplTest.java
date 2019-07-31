package ru.dsoccer1980.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.model.Vote;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.repository.VoteRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class VoteServiceImplTest {

    private final LocalDate DATE1 = LocalDate.of(2019, 7, 24);

    private final User USER1 = new User("Ivanov", "ivan@gmail.com", "password", Role.USER);
    private final User USER2 = new User("Petrov", "petr@gmail.com", "password2", Role.USER);
    private final Restaurant RESTAURANT1 = new Restaurant("TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant("Europe", "Mihailovskaja 14", USER2);
    private final Vote VOTE1 = new Vote(USER1, RESTAURANT1, DATE1);
    private final Vote VOTE2 = new Vote(USER2, RESTAURANT1, DATE1);
    private final Vote VOTE3 = new Vote(USER2, RESTAURANT2, LocalDate.of(2019, 7, 25));

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VoteService voteService;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        userRepository.save(USER1);
        userRepository.save(USER2);
        restaurantRepository.deleteAll();
        restaurantRepository.save(RESTAURANT1);
        restaurantRepository.save(RESTAURANT2);
        voteRepository.deleteAll();
        voteRepository.save(VOTE1);
        voteRepository.save(VOTE2);
        voteRepository.save(VOTE3);
    }

    @Test
    public void save() {
        LocalDate date = LocalDate.now().plusDays(1);
        Vote vote = voteService.save(USER1.getId(), RESTAURANT1.getId(), date);
        assertThat(voteService.getVotesByRestaurantAndDate(RESTAURANT1.getId(), date)).isEqualTo(Collections.singletonList(vote));
    }

    @Test
    @DisplayName("User changed his mind and voted for other restaurant")
    public void save2() {
        LocalDate date = LocalDate.now().plusDays(1);
        voteService.save(USER1.getId(), RESTAURANT1.getId(), date);
        Vote changedVote = voteService.save(USER1.getId(), RESTAURANT2.getId(), date);
        assertThat(voteService.getVotesByRestaurantAndDate(RESTAURANT2.getId(), date)).isEqualTo(Collections.singletonList(changedVote));
    }

    @Test
    public void delete() {
        voteService.delete(USER1.getId(), DATE1);
        List<Vote> votes = voteService.getVotesByRestaurantAndDate(RESTAURANT1.getId(), DATE1);
        assertThat(votes).isEqualTo(Collections.singletonList(VOTE2));
    }

    @Test
    public void getVotesByRestaurantAndByDate() {
        List<Vote> votes = voteService.getVotesByRestaurantAndDate(RESTAURANT1.getId(), DATE1);
        assertThat(votes).isEqualTo(Arrays.asList(VOTE1, VOTE2));
    }

    @Test
    public void getVotesByUserId() {
        List<Vote> votes = voteService.getVotesByUser(USER1.getId());
        assertThat(votes).isEqualTo(Collections.singletonList(VOTE1));
    }

    @Test
    public void getVotesByUserIdAndDate() {
        Vote vote = voteService.get(USER1.getId(), DATE1);
        assertThat(vote).isEqualTo(VOTE1);
    }

    @Test
    public void getVotesAmountForRestaurantsByDate() {
        Map<Restaurant, Long> votesAmount = voteService.getRestaurantVotesAmountByDate(DATE1);
        assertThat(votesAmount).isEqualTo(Map.of(RESTAURANT1, 2L));
    }

}