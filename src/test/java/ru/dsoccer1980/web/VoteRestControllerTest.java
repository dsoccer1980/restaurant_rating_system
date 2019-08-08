package ru.dsoccer1980.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.model.Vote;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.service.VoteService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({VoteRestController.class})
@MockBean(UserRepository.class)
class VoteRestControllerTest extends AbstractControllerTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final LocalDate VOTE_DATE1 = LocalDate.of(2019, 7, 24);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Collections.emptySet());
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Vote VOTE1 = new Vote(30L, USER1, RESTAURANT1, VOTE_DATE1);

    @MockBean
    private VoteService voteService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void createVote() throws Exception {
        given(voteService.save(-1L, 10L, LocalDate.of(2019, 8, 8))).willReturn(VOTE1);

        mvc.perform(post("/user/vote/restaurant/10/date/2019-08-08"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("10")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void getAllVotesByUser() throws Exception {
        given(voteService.getVotesByUser(-1L)).willReturn(List.of(VOTE1));

        mvc.perform(get("/user/vote"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(VOTE1.getUser().getName())));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void getRestaurantIdVotedUserByDate() throws Exception {
        given(voteService.getByUserIdAndDate(-1L, LocalDate.of(2019, 8, 8))).willReturn(Optional.of(VOTE1));

        mvc.perform(get("/user/vote/date/2019-08-08"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(VOTE1.getRestaurant().getId().toString())));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void getRestaurantIdVotedUserByDateWithEmptyResult() throws Exception {
        given(voteService.getByUserIdAndDate(-1L, LocalDate.of(2019, 8, 8))).willReturn(Optional.empty());

        mvc.perform(get("/user/vote/date/2019-08-08"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("-1")));
    }

    @Test
    void getDatesOfVotes() throws Exception {
        given(voteService.getDatesOfVotes()).willReturn(List.of(LocalDate.of(2019, 8, 8)));

        mvc.perform(get("/vote/date"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("2019-08-08")));
    }

    @Test
    void getRestaurantVotesAmountByDate() throws Exception {
        given(voteService.getRestaurantVotesAmountByDate(LocalDate.of(2019, 8, 8))).willReturn(Map.of("Teremok", 2L));

        mvc.perform(get("/vote/date/2019-08-08"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Teremok")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void deleteVote() throws Exception {
        given(voteService.delete(-1L, LocalDate.now())).willReturn(true);

        mvc.perform(delete("/user/vote"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }
}