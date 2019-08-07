package ru.dsoccer1980.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.repository.RestaurantRepository;
import ru.dsoccer1980.util.config.InitProps;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import({RestaurantServiceImpl.class})
class RestaurantServiceImplTest {

    private final Role ROLE_COMPANY = new Role(40L, InitProps.ROLE_COMPANY);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password",
            LocalDateTime.of(2019, 7, 31, 0, 0, 0), Set.of(ROLE_COMPANY));

    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);

    @Autowired
    private RestaurantService service;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @Test
    void get() {
        when(restaurantRepository.findById(RESTAURANT1.getId())).thenReturn(Optional.of(RESTAURANT1));
        assertThat(service.get(RESTAURANT1.getId())).isEqualTo(RESTAURANT1);
    }

    @Test
    void getNotExist() {
        when(restaurantRepository.findById(-1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.get(-1));
    }

    @Test
    void create() {
        User newUser = new User("new", "new@gmail.com", "password3", Set.of(ROLE_COMPANY));
        Restaurant savedRestaurant = new Restaurant(100L, "New", "Address", newUser);
        when(restaurantRepository.save(new Restaurant("New", "Address", newUser))).thenReturn(savedRestaurant);

        Restaurant newRestaurant = service.create(new Restaurant("New", "Address", newUser));
        assertThat(newRestaurant).isEqualTo(savedRestaurant);
    }

    @Test
    void update() {
        User newUser = new User("new", "new@gmail.com", "password3", Set.of(ROLE_COMPANY));
        Restaurant updatedRestaurant = new Restaurant(100L, "Updated", "Address", newUser);
        when(restaurantRepository.save(new Restaurant(100L, "New", "Address", newUser))).thenReturn(updatedRestaurant);

        assertThat(service.update(new Restaurant(100L, "New", "Address", newUser))).isEqualTo(updatedRestaurant);
    }

    @Test
    void delete() {
        doNothing().when(restaurantRepository).deleteById(RESTAURANT1.getId());
        when(restaurantRepository.findById(RESTAURANT1.getId())).thenThrow(new NotFoundException("not found"));

        service.delete(RESTAURANT1.getId());
        verify(restaurantRepository, times(1)).deleteById(RESTAURANT1.getId());
        assertThrows(NotFoundException.class, () -> service.get(RESTAURANT1.getId()));
    }

    @Test
    void deleteWithWrongId() {
        doThrow(EmptyResultDataAccessException.class).when(restaurantRepository).deleteById(-1L);
        assertThrows(NotFoundException.class, () -> service.delete(-1));
    }

    @Test
    void getRestaurantByUserId() {
        when(restaurantRepository.findRestaurantByUserId(USER1.getId())).thenReturn(Optional.of(RESTAURANT1));

        assertThat(service.getRestaurantByUserId(USER1.getId())).isEqualTo(Optional.of(RESTAURANT1));
    }
}