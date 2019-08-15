package ru.dsoccer1980.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.dsoccer1980.integration.MessageGateway;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.service.RoleService;
import ru.dsoccer1980.service.UserService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserRestController.class})
@MockBean(classes = {UserService.class, RoleService.class, UserRepository.class, MessageGateway.class})
class UserRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void saveUser() throws Exception {
        mvc.perform(post("/user")
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"new\", \"email\":\"new@g.ee\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void saveCompany() throws Exception {
        mvc.perform(post("/company")
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"new\", \"email\":\"new@g.ee\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
    }
}