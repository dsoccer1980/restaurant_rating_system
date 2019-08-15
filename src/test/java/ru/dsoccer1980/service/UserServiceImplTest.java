package ru.dsoccer1980.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dsoccer1980.domain.Role;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.util.config.InitProps;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import({UserServiceImpl.class})
@MockBean(classes={MeterRegistry.class})
class UserServiceImplTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final Role ROLE_USER = new Role(41L, InitProps.ROLE_USER);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Collections.emptySet());
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Collections.emptySet());

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    @Test
    void get() {
        when(userRepository.findById(USER1.getId())).thenReturn(Optional.of(USER1));

        assertThat(userService.get(USER1.getId())).isEqualTo(USER1);
    }

    @Test
    void update() {
        User updatedUser = new User(1L, "Updated", "ivan@gmail.com", "password", registeredTime, Collections.emptySet());
        when(userRepository.save(USER1)).thenReturn(updatedUser);

        assertThat(userService.update(USER1)).isEqualTo(updatedUser);
    }

    @Test
    void getAll() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(USER1, USER2));

        assertThat(userService.getAll()).isEqualTo(Arrays.asList(USER1, USER2));
    }

    @Test
    void create() {
        when(userRepository.save(USER1)).thenReturn(USER1);

        assertThat(userService.create(USER1)).isEqualTo(USER1);
    }


    @Test
    void delete() {
        doNothing().when(userRepository).deleteById(USER1.getId());
        when(userRepository.findById(USER1.getId())).thenThrow(new NotFoundException("not found"));

        userService.delete(USER1.getId());
        verify(userRepository, times(1)).deleteById(USER1.getId());
        assertThrows(NotFoundException.class, () -> userService.get(USER1.getId()));
    }

    @Test
    void deleteWithWrongId() {
        doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(-1L);

        assertThrows(NotFoundException.class, () -> userService.delete(-1));
    }

    @Test
    void getByEmail() {
        when(userRepository.findByEmail(USER1.getEmail())).thenReturn(USER1);

        assertThat(userService.getByEmail(USER1.getEmail())).isEqualTo(USER1);
    }
}