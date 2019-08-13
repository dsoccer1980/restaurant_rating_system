package ru.dsoccer1980.service.mail;

import ru.dsoccer1980.domain.User;


public interface EmailService {

    void sendMessage(User user);
}
