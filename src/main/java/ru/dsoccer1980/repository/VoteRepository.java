package ru.dsoccer1980.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote u WHERE u.user.id=:userId and u.date=:date")
    int delete(@Param("userId") long userId, @Param("date") LocalDate date);

    List<Vote> findByRestaurantIdAndDate(long id, LocalDate date);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant JOIN FETCH v.user WHERE v.user.id=:userId")
    List<Vote> findByUserId(@Param("userId") long userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.date=:date")
    List<Vote> findByDate(@Param("date") LocalDate date);

    Optional<Vote> findByUserIdAndDate(@Param("userId") long userId, @Param("date") LocalDate date);
}
