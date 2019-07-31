package ru.dsoccer1980;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.service.DishServiceImpl;
import ru.dsoccer1980.service.UserService;

import java.util.Optional;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);
//        DishServiceImpl service = context.getBean(DishServiceImpl.class);
//        service.getRestaurantsIntroducedTodayMenu().forEach(System.out::println);
    }
}
