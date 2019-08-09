package ru.dsoccer1980.service;


import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface VoteService {

    Vote save(long userId, long restaurantId, LocalDate date);

    boolean delete(long userId, LocalDate date);

    List<Vote> getVotesByRestaurantAndDate(long id, LocalDate date);

    List<Vote> getVotesByUser(long userId);

    Map<Restaurant, Long> getRestaurantVotesAmountByDate(LocalDate date);

    Vote get(long userId, LocalDate date);
}