package ru.dsoccer1980.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant u WHERE u.id=:id")
    int delete(@Param("id") long id);

    Restaurant findRestaurantByUserId(long userId);
}
