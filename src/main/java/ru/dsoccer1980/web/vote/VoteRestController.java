package ru.dsoccer1980.web.vote;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dsoccer1980.model.Vote;
import ru.dsoccer1980.service.VoteService;
import ru.dsoccer1980.web.AuthorizedUser;

import java.time.LocalDate;
import java.util.List;


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

  /*  @DeleteMapping(value = "/{date}")
    public void delete(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("delete vote for date {}", date);
        voteService.delete(AuthorizedUser.getId(), date);
    }

    @GetMapping(value = "/{date}")
    public Vote get(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        int userId = AuthorizedUser.getId();
        log.info("get vote for user {} and date", userId, date);
        return voteService.get(userId, date);
    }*/

}