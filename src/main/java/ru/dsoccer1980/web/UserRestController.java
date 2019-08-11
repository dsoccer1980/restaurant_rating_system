package ru.dsoccer1980.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.dsoccer1980.dto.UserDto;
import ru.dsoccer1980.integration.MessageGateway;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.security.AuthorizedUser;
import ru.dsoccer1980.service.RoleService;
import ru.dsoccer1980.service.UserService;
import ru.dsoccer1980.util.config.InitProps;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;
    private final MessageGateway messageGateway;

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        roleService.getByName(InitProps.ROLE_USER).ifPresent(role -> user.setRoles(Set.of(role)));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.create(user);
        messageGateway.process(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        long userId = AuthorizedUser.get().getId();
        userService.update(userId, userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/user")
    public UserDto getUserProfile() {
        return UserDto.getUserDto(userService.get(AuthorizedUser.get().getId()));
    }

    @PostMapping(value = "/company", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCompany(@RequestBody User user) {
        roleService.getByName(InitProps.ROLE_COMPANY).ifPresent(role -> user.setRoles(Set.of(role)));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.create(user);
        messageGateway.process(user);
        return ResponseEntity.ok().build();
    }
}
