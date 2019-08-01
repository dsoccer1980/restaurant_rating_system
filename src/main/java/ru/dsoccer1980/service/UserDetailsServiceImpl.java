package ru.dsoccer1980.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dsoccer1980.model.User;
import ru.dsoccer1980.repository.RoleRepository;
import ru.dsoccer1980.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean addUser(User user) {
        Optional<User> userFromDb = userRepository.findByName(user.getUsername());

        if (userFromDb.isEmpty()) {
            return false;
        }

        roleRepository.findByName("USER").ifPresent(role -> user.setRoles(Collections.singleton(role)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }


}
