package ru.dsoccer1980.service.mail;

import ru.dsoccer1980.model.User;


public interface EmailService {

    void sendMessage(User user);
}
