package ru.dsoccer1980.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.dsoccer1980.model.User;

import static java.util.Objects.requireNonNull;

public class AuthorizedUser {


    public static User get() {
        User user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    private static User safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof User) ? (User) principal : null;
    }


}
