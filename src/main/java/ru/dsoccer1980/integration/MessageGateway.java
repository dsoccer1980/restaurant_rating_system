package ru.dsoccer1980.integration;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.dsoccer1980.domain.User;

@MessagingGateway
public interface MessageGateway {

    @Gateway(requestChannel = "userChannel", replyChannel = "messageChannel")
    void process(User user);
}
