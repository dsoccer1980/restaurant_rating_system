package ru.dsoccer1980.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.domain.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Transactional
    int deleteByUserIdAndDate(long userId, LocalDate date);

    List<Vote> findByRestaurantIdAndDate(long id, LocalDate date);

    @EntityGraph("voteGraph")
    List<Vote> findByUserId(long userId);

    @EntityGraph("voteGraph")
    List<Vote> findByDate(LocalDate date);

    Optional<Vote> findByUserIdAndDate(long userId, LocalDate date);

    @Query(value = "SELECT v.date FROM Vote v GROUP BY v.date ORDER BY v.date DESC")
    List<LocalDate> findDatesOfVotes();
}
