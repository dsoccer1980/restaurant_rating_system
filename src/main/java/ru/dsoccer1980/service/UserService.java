package ru.dsoccer1980.service;


import ru.dsoccer1980.dto.UserDto;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.util.exception.NotFoundException;

import java.util.List;


public interface UserService {

    User get(long id) throws NotFoundException;

    User update(User user) throws NotFoundException;

    List<User> getAll();

    User create(User user);

    void delete(long id) throws NotFoundException;

    User getByEmail(String email);

    void update(long userId, UserDto userDto);
}