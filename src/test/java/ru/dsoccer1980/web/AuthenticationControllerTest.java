package ru.dsoccer1980.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.security.JwtTokenRequest;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthenticationController.class})
public class AuthenticationControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

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
    @WithMockUser(username = "company", authorities = {"ROLE_COMPANY"})
    void getRoleWithCompany() throws Exception {
        mvc.perform(get("/role").param("currentUser", "{\"name\":\"user1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("COMPANY")));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void getRoleWithUser() throws Exception {
        mvc.perform(get("/role").param("currentUser", "{\"name\":\"user1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("USER")));
    }

}
