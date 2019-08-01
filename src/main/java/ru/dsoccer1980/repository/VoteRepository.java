package ru.dsoccer1980.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    void deleteByUserIdAndDate(long userId, LocalDate date);

    List<Vote> findByRestaurantIdAndDate(long id, LocalDate date);

    @EntityGraph("voteGraph")
    List<Vote> findByUserId(long userId);

    @EntityGraph("voteGraph")
    List<Vote> findByDate(LocalDate date);

    Optional<Vote> findByUserIdAndDate(@Param("userId") long userId, @Param("date") LocalDate date);
}
