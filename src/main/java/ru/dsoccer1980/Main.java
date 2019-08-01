package ru.dsoccer1980;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.dsoccer1980.model.Restaurant;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.model.Vote;
import ru.dsoccer1980.repository.UserRepository;
import ru.dsoccer1980.repository.VoteRepository;
import ru.dsoccer1980.service.DishServiceImpl;
import ru.dsoccer1980.service.UserService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);
//        DishServiceImpl service = context.getBean(DishServiceImpl.class);
//        service.getRestaurantsIntroducedTodayMenu().forEach(System.out::println);

//        VoteRepository voteRepository = context.getBean(VoteRepository.class);
//        List<Vote> votes = voteRepository.findByDate(LocalDate.of(2019,7,31));
//        Map<Restaurant, Long> map = new HashMap<>();
//        votes.forEach(vote -> map.merge(vote.getRestaurant(), 1L, (k, v) -> v + 1));
//        map.forEach((k,v) -> System.out.println(k));
    }
}
