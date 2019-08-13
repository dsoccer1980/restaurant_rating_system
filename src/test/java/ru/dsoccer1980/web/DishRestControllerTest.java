package ru.dsoccer1980.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.dsoccer1980.domain.Dish;
import ru.dsoccer1980.domain.Restaurant;
import ru.dsoccer1980.domain.Role;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.service.DishService;
import ru.dsoccer1980.service.RestaurantService;
import ru.dsoccer1980.util.config.InitProps;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({DishRestController.class})
class DishRestControllerTest extends AbstractControllerTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final Role ROLE_USER = new Role(40L, InitProps.ROLE_USER);
    private final Role ROLE_COMPANY = new Role(41L, InitProps.ROLE_COMPANY);
    private final User USER1 = new User(1L, "user", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_USER));
    private final User COMPANY = new User(2L, "company", "petr@gmail.com", "password2", registeredTime, Set.of(ROLE_COMPANY));
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", COMPANY);
    private final Dish DISH1 = new Dish("Borsh", new BigDecimal(255.3).setScale(2, RoundingMode.FLOOR), RESTAURANT1, LocalDate.now());
    private final Dish DISH2 = new Dish(21L, "Soljanka", new BigDecimal(235.3).setScale(2, RoundingMode.FLOOR), RESTAURANT2, LocalDate.of(2019, 7, 23));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DishService dishService;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private UserRepository userRepository;

    @PostConstruct
    void postConstruct() {
        given(userRepository.findByName("company")).willReturn(Optional.of(COMPANY));
        given(userRepository.findByName("user")).willReturn(Optional.of(USER1));
    }


    @Test
    @WithUserDetails(value = "company")
    void createDishWithCompany() throws Exception {
        given(restaurantService.getRestaurantByUserId(COMPANY.getId())).willReturn(Optional.of(RESTAURANT2));

        mvc.perform(post("/company/dish")
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"null\",\"name\":\"name\",\"price\":\"2\",\"restaurant\":\"null\",\"date\":\"" + LocalDate.now() + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user")
    void createDishWithUser() throws Exception {
        mvc.perform(post("/company/dish")
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"null\",\"name\":\"name\",\"price\":\"2\",\"restaurant\":\"null\",\"date\":\"2019-07-30\"}"))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(value = "company")
    void deleteDishByIdWithCompany() throws Exception {
        mvc.perform(delete("/company/dish/100"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails(value = "user")
    void deleteDishByIdWithUser() throws Exception {
        mvc.perform(delete("/company/dish/100"))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(value = "company")
    void updateWithCompany() throws Exception {
        given(dishService.get(21L)).willReturn(DISH2);

        mvc.perform(put("/company/dish")
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"21\",\"name\":\"name\",\"price\":\"2\",\"restaurant\":\"null\",\"date\":\"2019-07-30\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user")
    void updateWithUser() throws Exception {
        mvc.perform(put("/company/dish")
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"21\",\"name\":\"name\",\"price\":\"2\",\"restaurant\":\"null\",\"date\":\"2019-07-30\"}"))
                .andExpect(status().is(403));
    }


    @Test
    @WithUserDetails(value = "company")
    void getDatesByRestaurantWithCompany() throws Exception {
        given(restaurantService.getRestaurantByUserId(COMPANY.getId())).willReturn(Optional.of(new Restaurant(100L, "name", "address", null)));
        given(dishService.getDatesByRestaurant(100))
                .willReturn(List.of(LocalDate.of(2019, 7, 30)));

        given(userRepository.findByName("company")).willReturn(Optional.of(USER1));

        mvc.perform(get("/company/dish/date"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("2019-07-30")));
    }

    @Test
    @WithUserDetails(value = "user")
    void getDatesByRestaurantWithUser() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(new Restaurant(100L, "name", "address", null)));
        given(dishService.getDatesByRestaurant(100))
                .willReturn(List.of(LocalDate.of(2019, 7, 30)));
        given(userRepository.findByName("company")).willReturn(Optional.of(USER1));

        mvc.perform(get("/company/dish/date"))
                .andExpect(status().is(403));
    }


    @Test
    @WithUserDetails(value = "company")
    void getDishByDateWithCompany() throws Exception {
        given(restaurantService.getRestaurantByUserId(COMPANY.getId())).willReturn(Optional.of(new Restaurant(1L, "name", "address", null)));
        given(dishService.getDishByRestaurantAndDate(1, LocalDate.of(2019, 7, 30)))
                .willReturn(List.of(new Dish("dish", BigDecimal.ONE, null, LocalDate.now())));

        mvc.perform(get("/company/dish/date/2019-07-30"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("dish")));
    }

    @Test
    @WithUserDetails(value = "user")
    void getDishByDateWithUser() throws Exception {
        given(dishService.getDishByRestaurantAndDate(1, LocalDate.of(2019, 7, 30)))
                .willReturn(List.of(new Dish("dish", BigDecimal.ONE, null, LocalDate.now())));

        mvc.perform(get("/company/dish/date/2019-07-30"))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(value = "company")
    void getDishByIdWithCompany() throws Exception {
        given(dishService.get(100))
                .willReturn(new Dish("dish", BigDecimal.ONE, null, LocalDate.now()));

        mvc.perform(get("/company/dish/100"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("dish")));
    }

    @Test
    @WithUserDetails(value = "user")
    void getDishByIdWithUser() throws Exception {
        mvc.perform(get("/company/dish/100"))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(value = "user")
    void getDishByRestaurantAndDateWithUser() throws Exception {
        given(dishService.getDishByRestaurantAndDate(1, LocalDate.of(2019, 7, 30)))
                .willReturn(List.of(new Dish("dish", BigDecimal.ONE, null, LocalDate.of(2019, 7, 30))));

        mvc.perform(get("/user/dish/restaurant/1/date/2019-07-30"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("dish")));
    }

    @Test
    @WithUserDetails(value = "company")
    void getDishByRestaurantAndDateWithCompany() throws Exception {
        mvc.perform(get("/user/dish/restaurant/1/date/2019-07-30"))
                .andExpect(status().is(403));

    }

    @Test
    @WithUserDetails(value = "user")
    void getRestaurantsIntroducedTodayMenuWithUser() throws Exception {
        given(dishService.getRestaurantsIntroducedTodayMenu())
                .willReturn(Set.of(RESTAURANT1));

        mvc.perform(get("/user/restaurant"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(RESTAURANT1.getName())));
    }

    @Test
    @WithUserDetails(value = "company")
    void getRestaurantsIntroducedTodayMenuWithCompany() throws Exception {
        mvc.perform(get("/user/restaurant"))
                .andExpect(status().is(403));
    }

}