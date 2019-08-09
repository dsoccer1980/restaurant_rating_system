package ru.dsoccer1980.service;

import org.springframework.stereotype.Service;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.model.Vote;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.repository.VoteRepository;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteServiceImpl(VoteRepository voteRepository,
                           UserRepository userRepository,
                           RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Vote save(long userId, long restaurantId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not found entity with id:" + userId));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Not found entity with id:" + restaurantId));
        Optional<Vote> voteByUserIdAndDate = voteRepository.findByUserIdAndDate(userId, date);
        Vote vote = voteByUserIdAndDate.orElse(new Vote(user, restaurant, date));
        vote.setRestaurant(restaurant);
        return voteRepository.save(vote);
    }

    @Override
    public boolean delete(long userId, LocalDate date) {
        return voteRepository.deleteByUserIdAndDate(userId, date) != 0;
    }

    @Override
    public List<Vote> getVotesByRestaurantAndDate(long id, LocalDate date) {
        return voteRepository.findByRestaurantIdAndDate(id, date);
    }

    @Override
    public List<Vote> getVotesByUser(long userId) {
        return voteRepository.findByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Vote::getDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<Restaurant, Long> getRestaurantVotesAmountByDate(LocalDate date) {
        List<Vote> votes = voteRepository.findByDate(date);
        Map<Restaurant, Long> map = new HashMap<>();
        votes.forEach(vote -> map.merge(vote.getRestaurant(), 1L, (k, v) -> v + 1));
        return map;
    }

    @Override
    public Vote get(long userId, LocalDate date) {
        return voteRepository.findByUserIdAndDate(userId, date).orElse(null);
    }

}