package ru.dsoccer1980.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.domain.Role;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.domain.Vote;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.service.VoteService;
import ru.dsoccer1980.util.config.InitProps;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({VoteRestController.class})
class VoteRestControllerTest extends AbstractControllerTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final LocalDate VOTE_DATE1 = LocalDate.of(2019, 7, 24);
    private final Role ROLE_COMPANY = new Role(40L, InitProps.ROLE_COMPANY);
    private final Role ROLE_USER = new Role(41L, InitProps.ROLE_USER);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_USER));
    private final User COMPANY = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_COMPANY));
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", COMPANY);
    private final Vote VOTE1 = new Vote(30L, USER1, RESTAURANT1, VOTE_DATE1);

    @MockBean
    private VoteService voteService;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;


    @PostConstruct
    void postConstruct() {
        given(userRepository.findByName("company")).willReturn(Optional.of(COMPANY));
        given(userRepository.findByName("user")).willReturn(Optional.of(USER1));
    }

    @Test
    @WithUserDetails(value = "user")
    void createVote() throws Exception {
        given(voteService.save(USER1.getId(), 10L, LocalDate.of(2019, 8, 8))).willReturn(VOTE1);

        mvc.perform(post("/user/vote/restaurant/10/date/2019-08-08"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("10")));
    }

    @Test
    @WithUserDetails(value = "user")
    void getAllVotesByUser() throws Exception {
        given(voteService.getVotesByUser(USER1.getId())).willReturn(List.of(VOTE1));

        mvc.perform(get("/user/vote"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(VOTE1.getUser().getName())));
    }

    @Test
    @WithUserDetails(value = "user")
    void getRestaurantIdVotedUserByDate() throws Exception {
        given(voteService.getByUserIdAndDate(USER1.getId(), LocalDate.of(2019, 8, 8))).willReturn(Optional.of(VOTE1));

        mvc.perform(get("/user/vote/date/2019-08-08"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(VOTE1.getRestaurant().getId().toString())));
    }

    @Test
    @WithUserDetails(value = "user")
    void getRestaurantIdVotedUserByDateWithEmptyResult() throws Exception {
        given(voteService.getByUserIdAndDate(USER1.getId(), LocalDate.of(2019, 8, 8))).willReturn(Optional.empty());

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
    @WithUserDetails(value = "user")
    void deleteVote() throws Exception {
        given(voteService.delete(USER1.getId(), LocalDate.now())).willReturn(true);

        mvc.perform(delete("/user/vote"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }
}