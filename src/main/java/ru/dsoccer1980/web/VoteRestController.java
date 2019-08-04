package ru.dsoccer1980.web;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Vote;
import ru.dsoccer1980.service.VoteService;
import ru.dsoccer1980.security.AuthorizedUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    private final VoteService voteService;

    public VoteRestController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(value = "/user/vote/restaurant/{restaurantId}/date/{date}")
    public void createVote(
            @PathVariable("restaurantId") int restaurantId,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        long userId = AuthorizedUser.get().getId();
        voteService.save(userId, restaurantId, date);
    }

    @GetMapping(value = "/user/vote")
    public List<Vote> getAllVotesByUser() {
        long userId = AuthorizedUser.get().getId();
        return voteService.getVotesByUser(userId);
    }

    @GetMapping(value = "/vote/date")
    public List<LocalDate> getDatesOfVotes() {
        return voteService.getDatesOfVotes();
    }

    @GetMapping(value = "/vote/date/{date}")
    public Map<String, Long> getRestaurantVotesAmountByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return voteService.getRestaurantVotesAmountByDate(date);

    }



}