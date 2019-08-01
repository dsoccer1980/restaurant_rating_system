package ru.dsoccer1980.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dsoccer1980.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
