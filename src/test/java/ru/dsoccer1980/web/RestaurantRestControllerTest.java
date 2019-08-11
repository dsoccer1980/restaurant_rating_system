package ru.dsoccer1980.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.Role;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.service.RestaurantService;
import ru.dsoccer1980.service.UserService;
import ru.dsoccer1980.util.config.InitProps;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({RestaurantRestController.class})
@MockBean(classes = {UserService.class})
class RestaurantRestControllerTest extends AbstractControllerTest {

    private final LocalDateTime registeredTime = LocalDateTime.of(2019, 7, 31, 0, 0, 0);
    private final Role ROLE_COMPANY = new Role(40L, InitProps.ROLE_COMPANY);
    private final Role ROLE_USER = new Role(41L, InitProps.ROLE_USER);
    private final User USER1 = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_USER));
    private final User COMPANY = new User(1L, "Ivanov", "ivan@gmail.com", "password", registeredTime, Set.of(ROLE_COMPANY));
    private final Restaurant RESTAURANT1 = new Restaurant(10L, "TSAR", "Nevskij 53", COMPANY);

    @Autowired
    private MockMvc mvc;

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
    void createRestaurantWithCompany() throws Exception {
        given(restaurantService.getRestaurantByUserId(COMPANY.getId())).willReturn(Optional.of(RESTAURANT1));

        mvc.perform(post("/company/restaurant")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(RESTAURANT1)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "user")
    void createRestaurantWithUser() throws Exception {
        mvc.perform(post("/company/restaurant")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(RESTAURANT1)))
                .andExpect(status().is(403));
    }

    @Test
    @WithUserDetails(value = "company")
    void hasUserRestaurantWithCompanyDoesntHas() throws Exception {
        given(restaurantService.getRestaurantByUserId(COMPANY.getId())).willReturn(Optional.empty());

        mvc.perform(get("/company/user/restaurant"))
                .andExpect(status().is(404));
    }

    @Test
    @WithUserDetails(value = "user")
    void hasUserRestaurantWithUser() throws Exception {
        mvc.perform(get("/company/user/restaurant"))
                .andExpect(status().is(403));
    }


}