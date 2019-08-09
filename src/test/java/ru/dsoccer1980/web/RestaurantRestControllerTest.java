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
import ru.dsoccer1980.service.RestaurantService;
import ru.dsoccer1980.service.UserService;
import ru.dsoccer1980.util.config.InitProps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({RestaurantRestController.class})
class RestaurantRestControllerTest extends AbstractControllerTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final Role ROLE_COMPANY = new Role(40L, InitProps.ROLE_COMPANY);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_COMPANY));
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", USER1);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void createRestaurantWithCompany() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(RESTAURANT1));
        given(userService.get(-1L)).willReturn(USER1);

        mvc.perform(post("/company/restaurant")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(RESTAURANT1)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void createRestaurantWithUser() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(RESTAURANT1));
        given(userService.get(-1L)).willReturn(USER1);

        mvc.perform(post("/company/restaurant")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(RESTAURANT1)))
                .andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void hasUserRestaurantWithCompanyDoesntHas() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.empty());

        mvc.perform(get("/company/user/restaurant"))
                .andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void hasUserRestaurantWithUser() throws Exception {
        given(restaurantService.getRestaurantByUserId(-1L)).willReturn(Optional.of(new Restaurant(100L, "name", "address", null)));

        mvc.perform(get("/company/user/restaurant"))
                .andExpect(status().is(403));
    }


}