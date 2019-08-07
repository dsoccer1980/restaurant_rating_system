package ru.dsoccer1980.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dsoccer1980.dto.UserDto;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.util.List;
import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User get(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found user with id:" + id));
    }

    @Override
    public User update(User user) throws NotFoundException {
        Objects.requireNonNull(user, "user must not be null");
        return repository.save(user);
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User create(User user) {
        Objects.requireNonNull(user, "user must not be null");
        return repository.save(user);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Not found entity with id:" + id);
        }
    }

    @Override
    public User getByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void update(long userId, UserDto userDto) {
        Objects.requireNonNull(userDto, "user must not be null");
        User userFromDb = repository.findById(userId).orElseThrow(() -> new NotFoundException("user id not found"));
        userFromDb.setName(userDto.getName());
        if (!userFromDb.getPassword().equals(userDto.getPassword())) {
            userFromDb.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        }
        userFromDb.setEmail(userDto.getEmail());
        repository.save(userFromDb);

    }

}