package ru.dsoccer1980.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dsoccer1980.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish u WHERE u.id=:id")
    int delete(@Param("id") long id);

    List<Dish> findDishByRestaurantIdAndDate(Long id, LocalDate date);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.date=:date")
    List<Dish> findDishByDate(@Param("date") LocalDate date);

    List<Dish> findDishByRestaurantId(Long id);

}
