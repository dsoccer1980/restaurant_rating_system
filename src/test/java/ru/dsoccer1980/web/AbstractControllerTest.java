package ru.dsoccer1980.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.dsoccer1980.security.JWTWebSecurityConfig;
import ru.dsoccer1980.security.JwtTokenUtil;
import ru.dsoccer1980.security.JwtUnAuthorizedResponseAuthenticationEntryPoint;
import ru.dsoccer1980.service.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@Import({JWTWebSecurityConfig.class, JwtUnAuthorizedResponseAuthenticationEntryPoint.class,
        UserDetailsServiceImpl.class, JwtTokenUtil.class})
public class AbstractControllerTest {

    static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
