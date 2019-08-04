package ru.dsoccer1980.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findDishByRestaurantIdAndDate(Long id, LocalDate date);

    @EntityGraph("dishGraph")
    List<Dish> findDishByDate(@Param("date") LocalDate date);

    @Query("SELECT d.date FROM Dish d WHERE d.restaurant.id=:restaurantId GROUP BY d.date ORDER BY d.date")
    List<LocalDate> findDatesByRestaurantId(@Param("restaurantId") Long restaurantId);



}
