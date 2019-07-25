package ru.dsoccer1980.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan({"ru.dsoccer1980.repository"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    private final User USER1 = new User("Ivanov", "ivan@gmail.com", "password", Role.USER);
    private final User USER2 = new User("Petrov", "petr@gmail.com", "password2", Role.USER);
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
        repository.save(USER1);
        repository.save(USER2);
    }

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


}
