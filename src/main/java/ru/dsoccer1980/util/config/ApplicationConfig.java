package ru.dsoccer1980.util.config;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.dsoccer1980.service.mail.EmailService;
import ru.dsoccer1980.service.mail.EmailServiceImpl;

@Configuration
public class ApplicationConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }

    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl();
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> saveBookRegistry() {
        return registry -> registry.config().namingConvention().name("services.user.registration", Meter.Type.COUNTER);
    }
}
