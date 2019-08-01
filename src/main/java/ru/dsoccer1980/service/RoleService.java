package ru.dsoccer1980.service;

import ru.dsoccer1980.model.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> getByName(String name);
}
