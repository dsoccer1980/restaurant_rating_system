package ru.dsoccer1980.service;

import org.springframework.stereotype.Service;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.repository.RoleRepository;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Role> getByName(String name) {
        return repository.findByName(name);
    }
}
