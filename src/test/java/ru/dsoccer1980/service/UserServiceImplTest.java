package ru.dsoccer1980.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceImplTest extends AbstractServiceTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Collections.emptySet());
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Collections.emptySet());

    @Autowired
    private UserService userService;


    @Test
    void get() {
        assertThat(userService.get(USER1.getId())).isEqualTo(USER1);
    }

    @Test
    void update() {
        User userForUpdate = userService.get(USER1.getId());
        userForUpdate.setName("New Name");
        userService.update(userForUpdate);

        assertThat(userService.get(USER1.getId())).isEqualTo(userForUpdate);
    }

    @Test
    void getAll() {
        assertThat(userService.getAll()).isEqualTo(Arrays.asList(USER1, USER2));
    }

    @Test
    void create() {
        User newUser = userService.create(new User(3L, "new User", "new@gmail.com", "password3", registeredTime, Set.of(Role.USER)));
        assertThat(userService.getAll()).isEqualTo(Arrays.asList(USER1, USER2, newUser));
    }


    @Test
    void delete() {
        userService.delete(USER1.getId());
        assertThat(userService.getAll()).isEqualTo(Collections.singletonList(USER2));
    }

    @Test
    void deleteWithWrongId() {
        assertThrows(NotFoundException.class, () -> userService.delete(-1));
    }

    @Test
    void getByEmail() {
        assertThat(userService.getByEmail(USER1.getEmail())).isEqualTo(USER1);
    }
}