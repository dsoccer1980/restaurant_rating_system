package ru.dsoccer1980;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.dsoccer1980.repository.RestaurantRepository;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);
        RestaurantRepository repository = context.getBean(RestaurantRepository.class);
        System.out.println(repository.findAll());
    }
}
