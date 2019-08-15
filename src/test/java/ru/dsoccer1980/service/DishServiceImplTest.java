package ru.dsoccer1980.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dsoccer1980.domain.Dish;
import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.domain.Role;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.repository.DishRepository;
import ru.dsoccer1980.util.config.InitProps;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import({DishServiceImpl.class})
public class DishServiceImplTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final Role ROLE_COMPANY = new Role(40L, InitProps.ROLE_COMPANY);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_COMPANY));
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Set.of(ROLE_COMPANY));
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);

    private final Dish DISH1 = new Dish(20L, "Borsh", new BigDecimal(255.3).setScale(2, RoundingMode.FLOOR), RESTAURANT1, LocalDate.of(2019, 7, 24));

    @Autowired
    private DishService dishService;

    @MockBean
    private DishRepository dishRepository;

    @Test
    void getById() {
        when(dishRepository.findById(DISH1.getId())).thenReturn(Optional.of(DISH1));
        assertThat(dishService.get(DISH1.getId())).isEqualTo(DISH1);
    }

    @Test
    void getNotExist() {
        when(dishRepository.findById(DISH1.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> dishService.get(-1));
    }

    @Test
    void create() {
        Dish dishForSave = new Dish("New dish", new BigDecimal(270.0), RESTAURANT1, LocalDate.of(2019, 7, 25));
        Dish returnedDish = new Dish(100L, "New dish", new BigDecimal(270.0), RESTAURANT1, LocalDate.of(2019, 7, 25));
        when(dishRepository.save(dishForSave)).thenReturn(returnedDish);
        assertThat(dishService.create(dishForSave)).isEqualTo(returnedDish);
    }

    @Test
    void update() {
        Dish dishForUpdate = new Dish(100L, "dish", new BigDecimal(270.0), RESTAURANT1, LocalDate.of(2019, 7, 25));
        Dish returnedDish = new Dish(100L, "Updated dish", new BigDecimal(270.0), RESTAURANT1, LocalDate.of(2019, 7, 25));
        when(dishRepository.save(dishForUpdate)).thenReturn(returnedDish);
        assertThat(dishService.update(dishForUpdate)).isEqualTo(returnedDish);
    }

    @Test
    void delete() {
        doNothing().when(dishRepository).deleteById(DISH1.getId());
        when(dishRepository.findById(DISH1.getId())).thenThrow(new NotFoundException("not found"));

        dishService.delete(DISH1.getId());
        verify(dishRepository, times(1)).deleteById(DISH1.getId());
        assertThrows(NotFoundException.class, () -> dishService.get(DISH1.getId()));
    }

    @Test
    void deleteWithWrongId() {
        doThrow(EmptyResultDataAccessException.class).when(dishRepository).deleteById(-1L);
        assertThrows(NotFoundException.class, () -> dishService.delete(-1));
    }

    @Test
    void findDishByRestaurantIdAndDate() {
        when(dishRepository.findDishByRestaurantIdAndDate(RESTAURANT1.getId(), LocalDate.of(2019, 7, 24)))
                .thenReturn(Collections.singletonList(DISH1));
        List<Dish> result = dishService.getDishByRestaurantAndDate(RESTAURANT1.getId(), LocalDate.of(2019, 7, 24));
        assertThat(result).isEqualTo(Collections.singletonList(DISH1));
    }

    @Test
    void getRestaurantsIntroducedTodayMenu() {
        when(dishRepository.findDishByDate(LocalDate.now())).thenReturn(List.of(new Dish("Today dish", new BigDecimal(235.3).setScale(2, RoundingMode.FLOOR), RESTAURANT2, LocalDate.now())));

        Set<Restaurant> restaurants = dishService.getRestaurantsIntroducedTodayMenu();
        assertThat(restaurants).isEqualTo(Set.of(RESTAURANT2));
    }

    @Test
    void getDatesByRestaurant() {
        when(dishRepository.findDatesByRestaurantId(RESTAURANT1.getId())).thenReturn(Collections.singletonList(LocalDate.of(2019, 7, 24)));
        List<LocalDate> datesByRestaurant = dishService.getDatesByRestaurant(RESTAURANT1.getId());
        assertThat(datesByRestaurant).isEqualTo(Collections.singletonList(LocalDate.of(2019, 7, 24)));
    }


}
