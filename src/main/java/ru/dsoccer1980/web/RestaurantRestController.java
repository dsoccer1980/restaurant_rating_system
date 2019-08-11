package ru.dsoccer1980.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.security.AuthorizedUser;
import ru.dsoccer1980.service.RestaurantService;
import ru.dsoccer1980.service.UserService;

import java.util.Optional;

@RestController
public class RestaurantRestController {

    private final RestaurantService restaurantService;
    private final UserService userService;

    public RestaurantRestController(RestaurantService restaurantService, UserService userService) {
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    @PostMapping(value = "/company/restaurant", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRestaurant(@RequestBody Restaurant restaurant) {
        long userId = AuthorizedUser.get().getId();
        restaurant.setUser(userService.get(userId));

        restaurantService.create(restaurant);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/company/restaurant", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateRestaurant(@RequestBody Restaurant restaurant) {
        long userId = AuthorizedUser.get().getId();
        restaurant.setUser(userService.get(userId));

        restaurantService.update(restaurant);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/company/restaurant")
    public ResponseEntity<?> getRestaurant() {
        long userId = AuthorizedUser.get().getId();

        Optional<Restaurant> restaurantByUserId = restaurantService.getRestaurantByUserId(userId);
        if (restaurantByUserId.isPresent()) {
            return ResponseEntity.ok(restaurantByUserId.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
