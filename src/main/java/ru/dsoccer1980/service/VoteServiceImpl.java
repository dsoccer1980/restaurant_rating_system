package ru.dsoccer1980.service;

import org.springframework.stereotype.Service;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.model.Vote;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.repository.VoteRepository;
import ru.dsoccer1980.util.exception.NotFoundException;
import ru.dsoccer1980.util.exception.VoteException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class VoteServiceImpl implements VoteService {

    private final LocalTime DEADLINE = LocalTime.of(11, 0);

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
        if (canVote(date)) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("Not found entity with id:" + userId));
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new NotFoundException("Not found entity with id:" + restaurantId));
            Optional<Vote> voteByUserIdAndDate = voteRepository.findByUserIdAndDate(userId, date);
            Vote vote = voteByUserIdAndDate.orElse(new Vote(user, restaurant, date));
            vote.setRestaurant(restaurant);
            return voteRepository.save(vote);
        } else {
            throw new VoteException("you can not vote this date");
        }
    }

    @Override
    public Vote save(Vote vote) {
        if (canVote(vote.getDate())) {
            return voteRepository.save(vote);
        } else {
            throw new VoteException("you can not vote this date");
        }
    }

    @Override
    public boolean delete(long userId, LocalDate date) {
        return voteRepository.delete(userId, date) != 0;
    }

    @Override
    public List<Vote> getVotesByRestaurantAndDate(long id, LocalDate date) {
        return voteRepository.findByRestaurantIdAndDate(id, date);
    }

    @Override
    public List<Vote> getVotesByUser(long userId) {
        return voteRepository.findByUserId(userId);
    }

    @Override
    public Map<Restaurant, Long> getRestaurantVotesAmountByDate(LocalDate date) {
        List<Vote> votes = voteRepository.findByDate(date);
        Map<Restaurant, Long> map = new ConcurrentHashMap<>();
        votes.forEach(vote -> map.merge(vote.getRestaurant(), 1L, (k, v) -> v + 1));
        return map;
    }

    @Override
    public Vote get(long userId, LocalDate date) {
        return voteRepository.findByUserIdAndDate(userId, date).orElse(null);
    }

    private boolean canVote(LocalDate date) {
        return LocalDateTime.now().isBefore(LocalDateTime.of(date, DEADLINE));
    }
}