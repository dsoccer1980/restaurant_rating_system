package ru.dsoccer1980.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.dsoccer1980.domain.Role;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.security.JwtTokenRequest;
import ru.dsoccer1980.util.config.InitProps;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthenticationController.class})
public class AuthenticationControllerTest extends AbstractControllerTest {

    private final Role ROLE_USER = new Role(40L, InitProps.ROLE_USER);
    private final Role ROLE_COMPANY = new Role(41L, InitProps.ROLE_COMPANY);
    private final User USER1 = new User(1L, "user", "ivan@gmail.com", "password", LocalDateTime.now(), Set.of(ROLE_USER));
    private final User COMPANY = new User(2L, "company", "petr@gmail.com", "password2", LocalDateTime.now(), Set.of(ROLE_COMPANY));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @PostConstruct
    void postConstruct() {
        given(userRepository.findByName("company")).willReturn(Optional.of(COMPANY));
        given(userRepository.findByName("user")).willReturn(Optional.of(USER1));
    }

    @Test
    void testAuthenticateUser() throws Exception {
        when(userRepository.findByName("user"))
                .thenReturn(Optional.of(new User("user", "user@gmail.com", new BCryptPasswordEncoder().encode("password"), Collections.emptySet())));

        mvc.perform(post("/authenticate")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(new JwtTokenRequest("user", "password"))))
                .andExpect(status().isOk());
    }

    @Test
    void testAuthenticateWithWrongPassword() throws Exception {
        when(userRepository.findByName("user"))
                .thenReturn(Optional.of(new User("user", "user@gmail.com", new BCryptPasswordEncoder().encode("password"), Collections.emptySet())));

        mvc.perform(post("/authenticate")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(new JwtTokenRequest("user", "wrong password"))))
                .andExpect(status().is(401));
    }

    @Test
    @WithUserDetails(value = "company")
    void getRoleWithCompany() throws Exception {
        mvc.perform(get("/role").param("currentUser", "{\"name\":\"user1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("COMPANY")));
    }

    @Test
    @WithUserDetails(value = "user")
    void getRoleWithUser() throws Exception {
        mvc.perform(get("/role").param("currentUser", "{\"name\":\"user1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("USER")));
    }

}
