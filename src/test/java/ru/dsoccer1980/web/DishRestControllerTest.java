package ru.dsoccer1980.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.dsoccer1980.model.Dish;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.service.DishService;
import ru.dsoccer1980.service.RestaurantService;
import ru.dsoccer1980.util.config.InitProps;

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
    private final Role ROLE_COMPANY = new Role(40L, InitProps.ROLE_COMPANY);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_COMPANY));
    private final User USER2 = new User(2L, "Petrov", "petr@gmail.com", "password2", registeredTime, Set.of(ROLE_COMPANY));
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);
    private final Restaurant RESTAURANT2 = new Restaurant(11L, "Europe", "Mihailovskaja 14", USER2);
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


    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void createDishWithCompany() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(RESTAURANT2));

        mvc.perform(post("/company/dish")
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"null\",\"name\":\"name\",\"price\":\"2\",\"restaurant\":\"null\",\"date\":\"" + LocalDate.now() + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void createDishWithUser() throws Exception {
        mvc.perform(post("/company/dish")
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"null\",\"name\":\"name\",\"price\":\"2\",\"restaurant\":\"null\",\"date\":\"2019-07-30\"}"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void deleteDishByIdWithCompany() throws Exception {
        mvc.perform(delete("/company/dish/100"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void deleteDishByIdWithUser() throws Exception {
        mvc.perform(delete("/company/dish/100"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void updateWithCompany() throws Exception {
        given(dishService.get(21L)).willReturn(DISH2);
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(RESTAURANT2));

        mvc.perform(put("/company/dish")
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"21\",\"name\":\"name\",\"price\":\"2\",\"restaurant\":\"null\",\"date\":\"2019-07-30\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void updateWithUser() throws Exception {
        mvc.perform(put("/company/dish")
                .contentType(APPLICATION_JSON)
                .content("{\"id\":\"21\",\"name\":\"name\",\"price\":\"2\",\"restaurant\":\"null\",\"date\":\"2019-07-30\"}"))
                .andExpect(status().is(403));
    }


    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void getDatesByRestaurantWithCompany() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(new Restaurant(100L, "name", "address", null)));
        given(dishService.getDatesByRestaurant(100))
                .willReturn(List.of(LocalDate.of(2019, 7, 30)));

        given(userRepository.findByName("company")).willReturn(Optional.of(USER1));

        mvc.perform(get("/company/dish/date"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("2019-07-30")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void getDatesByRestaurantWithUser() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(new Restaurant(100L, "name", "address", null)));
        given(dishService.getDatesByRestaurant(100))
                .willReturn(List.of(LocalDate.of(2019, 7, 30)));
        given(userRepository.findByName("company")).willReturn(Optional.of(USER1));

        mvc.perform(get("/company/dish/date"))
                .andExpect(status().is(403));
    }


    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void getDishByDateWithCompany() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(new Restaurant(1L, "name", "address", null)));
        given(dishService.getDishByRestaurantAndDate(1, LocalDate.of(2019, 7, 30)))
                .willReturn(List.of(new Dish("dish", BigDecimal.ONE, null, LocalDate.now())));

        mvc.perform(get("/company/dish/date/2019-07-30"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("dish")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void getDishByDateWithUser() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(new Restaurant(1L, "name", "address", null)));
        given(dishService.getDishByRestaurantAndDate(1, LocalDate.of(2019, 7, 30)))
                .willReturn(List.of(new Dish("dish", BigDecimal.ONE, null, LocalDate.now())));

        mvc.perform(get("/company/dish/date/2019-07-30"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void getDishByIdWithCompany() throws Exception {
        given(dishService.get(100))
                .willReturn(new Dish("dish", BigDecimal.ONE, null, LocalDate.now()));

        mvc.perform(get("/company/dish/100"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("dish")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void getDishByIdWithUser() throws Exception {


        mvc.perform(get("/company/dish/100"))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void getDishByRestaurantAndDateWithUser() throws Exception {
        given(dishService.getDishByRestaurantAndDate(1, LocalDate.of(2019, 7, 30)))
                .willReturn(List.of(new Dish("dish", BigDecimal.ONE, null, LocalDate.of(2019, 7, 30))));

        mvc.perform(get("/user/dish/restaurant/1/date/2019-07-30"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("dish")));
    }

    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void getDishByRestaurantAndDateWithCompany() throws Exception {
        mvc.perform(get("/user/dish/restaurant/1/date/2019-07-30"))
                .andExpect(status().is(403));

    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void getRestaurantsIntroducedTodayMenuWithUser() throws Exception {
        given(dishService.getRestaurantsIntroducedTodayMenu())
                .willReturn(Set.of(RESTAURANT1));

        mvc.perform(get("/user/restaurant"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(RESTAURANT1.getName())));
    }

    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void getRestaurantsIntroducedTodayMenuWithCompany() throws Exception {
        mvc.perform(get("/user/restaurant"))
                .andExpect(status().is(403));
    }

}