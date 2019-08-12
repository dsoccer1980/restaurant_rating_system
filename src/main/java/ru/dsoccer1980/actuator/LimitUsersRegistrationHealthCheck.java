package ru.dsoccer1980.actuator;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.dsoccer1980.repository.UserRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class LimitUsersRegistrationHealthCheck implements HealthIndicator {

    private static final int USERS_LIMIT = 3;
    private final UserRepository repository;

    @Override
    public Health health() {
        Map<LocalDate, Long> collect = repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(u -> u.getRegistrationTime().toLocalDate(), Collectors.counting()));

        Map<LocalDate, Long> result = collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > USERS_LIMIT)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        return result.size() != 0 ?
                Health.up().withDetail("Too many users", result).build() :
                Health.down().withDetail("Count of user is good", "").build();
    }
}
