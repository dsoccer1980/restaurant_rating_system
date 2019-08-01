package ru.dsoccer1980.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends AbstractDataJpaTest {

    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", LocalDate.of(2019, 7, 31), Collections.emptySet());
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", LocalDate.of(2019, 7, 31), Collections.emptySet());

    @Autowired
    private UserRepository repository;

    @Test
    void getAll() {
        assertThat(repository.findAll()).isEqualTo(Arrays.asList(USER1, USER2));
    }

    @Test
    void getById() {
        assertThat(repository.findById(USER1.getId()).orElse(null)).isEqualTo(USER1);
    }

    @Test
    void create() {
        User newUser = repository.save(new User("new User", "new@gmail.com", "password3", Role.USER));
        assertThat(repository.findAll()).isEqualTo(Arrays.asList(USER1, USER2, newUser));
    }

    @Test
    void update() {
        User userForUpdate = repository.findById(USER1.getId()).orElse(null);
        userForUpdate.setName("New Name");
        repository.save(userForUpdate);

        assertThat(repository.findById(USER1.getId()).orElse(null)).isEqualTo(userForUpdate);
    }

    @Test
    void delete() {
        repository.delete(USER1);
        assertThat(repository.findAll()).isEqualTo(Collections.singletonList(USER2));
    }

    @Test
    void deleteWithWrongId() {
        assertThat(repository.delete(-1)).isEqualTo(0);
        assertThat(repository.findAll()).isEqualTo(Arrays.asList(USER1, USER2));
    }


}
