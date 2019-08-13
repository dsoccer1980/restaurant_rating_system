package ru.dsoccer1980.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dsoccer1980.dto.UserDto;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRestAdminController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public List<User> getAll() {
        return userService.getAll();
    }

    @PutMapping(value = "/admin/user")
    public ResponseEntity<?> updateUserByAdmin(@RequestBody UserDto userDto) {
        userService.update(userDto.getId(), userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/admin/user/{id}")
    public UserDto getUserProfileByAdmin(@PathVariable("id") long id) {
        return UserDto.getUserDto(userService.get(id));
    }


    @DeleteMapping(value = "/admin/user/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("id") long id) {
        userService.delete(id);
    }
}
