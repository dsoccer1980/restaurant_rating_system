package ru.dsoccer1980.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    private final User USER1 = new User("Ivanov", "ivan1@gmail.com", "password", Role.USER);
    private final User USER2 = new User("Petrov", "petr1@gmail.com", "password2", Role.USER);
    @Autowired
    private UserRepository repository;
    @Autowired
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
        repository.save(USER1);
        repository.save(USER2);
    }

    @AfterEach
    void afterEach() {
        repository.deleteAll();
    }

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
        User newUser = userService.create(new User("new User", "new@gmail.com", "password3", Role.USER));
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