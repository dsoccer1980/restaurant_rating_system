package ru.dsoccer1980.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class ActuactorRestController {

    private final RestTemplate restTemplate;

    @GetMapping("/admin/actuator/health")
    public String getHealth(HttpServletRequest request) {
        String host = getHost(request, "/admin");
        return restTemplate.getForObject(host + "/actuator/health/limitUsersRegistrationHealthCheck", String.class);
    }

    @GetMapping("/admin/actuator/metrics")
    public String getMetrics(HttpServletRequest request) {
        String host = getHost(request, "/admin");
        return restTemplate.getForObject(host + "/actuator/metrics/services.user.registration", String.class);
    }

    private String getHost(HttpServletRequest request, String str) {
        String url = request.getRequestURL().toString();
        return url.substring(0, url.indexOf(str));
    }
}
