package ru.dsoccer1980.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.domain.Vote;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({VoteServiceImpl.class})
public class VoteServiceImplTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Collections.emptySet());
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Collections.emptySet());
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);

    private final LocalDate DATE1 = LocalDate.of(2019, 7, 24);
    private final Vote VOTE1 = new Vote(30L, USER1, RESTAURANT1, DATE1);
    private final Vote VOTE2 = new Vote(31L, USER2, RESTAURANT1, DATE1);

    @Autowired
    private VoteService voteService;

    @MockBean
    private VoteRepository voteRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RestaurantRepository restaurantRepository;

    @Test
    void save() {
        when(userRepository.findById(USER1.getId())).thenReturn(Optional.of(USER1));
        when(restaurantRepository.findById(RESTAURANT1.getId())).thenReturn(Optional.of(RESTAURANT1));
        when(voteRepository.findByUserIdAndDate(USER1.getId(), LocalDate.now().plusDays(1))).thenReturn(Optional.of(VOTE1));
        when(voteRepository.save(VOTE1)).thenReturn(VOTE1);

        assertThat(voteService.save(USER1.getId(), RESTAURANT1.getId(), LocalDate.now().plusDays(1))).isEqualTo(VOTE1);

    }

    @Test
    void delete() {
        when(voteRepository.deleteByUserIdAndDate(USER1.getId(), DATE1)).thenReturn(1);

        assertTrue(voteService.delete(USER1.getId(), DATE1));
    }

    @Test
    void deleteWithWrongData() {
        when(voteRepository.deleteByUserIdAndDate(-1, DATE1)).thenReturn(0);

        assertFalse(voteService.delete(USER1.getId(), DATE1));
    }

    @Test
    void getVotesByRestaurantAndByDate() {
        when(voteRepository.findByRestaurantIdAndDate(RESTAURANT1.getId(), DATE1)).thenReturn(Arrays.asList(VOTE1, VOTE2));

        assertThat(voteService.getVotesByRestaurantAndDate(RESTAURANT1.getId(), DATE1)).isEqualTo(Arrays.asList(VOTE1, VOTE2));
    }

    @Test
    void getVotesByUserId() {
        when(voteRepository.findByUserId(USER1.getId())).thenReturn(Collections.singletonList(VOTE1));

        assertThat(voteService.getVotesByUser(USER1.getId())).isEqualTo(Collections.singletonList(VOTE1));
    }

    @Test
    void getVotesByUserIdAndDate() {
        when(voteRepository.findByUserIdAndDate(USER1.getId(), DATE1)).thenReturn(Optional.of(VOTE1));

        assertThat(voteService.getByUserIdAndDate(USER1.getId(), DATE1)).isEqualTo(Optional.of(VOTE1));
    }

    @Test
    void getVotesAmountForRestaurantsByDate() {
        when(voteRepository.findByDate(DATE1)).thenReturn(List.of(VOTE1, VOTE2));

        assertThat(voteService.getRestaurantVotesAmountByDate(DATE1)).isEqualTo(Map.of(RESTAURANT1.getName(), 2L));
    }

}