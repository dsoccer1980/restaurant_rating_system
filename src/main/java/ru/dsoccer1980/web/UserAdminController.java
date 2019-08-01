package ru.dsoccer1980.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.service.RoleService;
import ru.dsoccer1980.service.UserService;
import ru.dsoccer1980.util.config.InitProps;

import java.util.Set;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAdminController {

    private final UserService userService;
    private final RoleService roleService;

    public UserAdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping(value = "/admin/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody User user) {
        roleService.getByName(InitProps.ROLE_USER).ifPresent(role -> user.setRoles(Set.of(role)));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.create(user);
        return ResponseEntity.ok().build();
    }
}
