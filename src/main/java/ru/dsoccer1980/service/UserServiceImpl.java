package ru.dsoccer1980.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.util.List;

import static ru.dsoccer1980.util.ValidationUtil.checkNotFoundWithId;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User get(long id) throws NotFoundException {
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("Not found user with id:" + id));
        return checkNotFoundWithId(user, id);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public User update(User user) throws NotFoundException {
        Assert.notNull(user, "user must not be null");
        return checkNotFoundWithId(repository.save(user), user.getId());
    }

    @Cacheable("users")
    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void delete(long id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public User getByEmail(String email) {
        return repository.findByEmail(email);
    }

}