package ru.dsoccer1980.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Restaurant;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findRestaurantByUserId(long userId);
}
