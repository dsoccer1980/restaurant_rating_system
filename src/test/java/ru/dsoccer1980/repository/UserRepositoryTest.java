package ru.dsoccer1980.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.dsoccer1980.domain.Role;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.util.config.InitProps;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserRepositoryTest extends AbstractDataJpaTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final Role ROLE_USER = new Role(41L, InitProps.ROLE_USER);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Collections.emptySet());
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Collections.emptySet());

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
        User newUser = repository.save(new User("new User", "new@gmail.com", "password3", Set.of(ROLE_USER)));
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
        assertThrows(EmptyResultDataAccessException.class, () -> repository.deleteById(-1L));
        assertThat(repository.findAll()).isEqualTo(Arrays.asList(USER1, USER2));
    }


}
