package com.example.expenses.service;

import com.example.expenses.model.User;
import com.example.expenses.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public boolean loginTaken(String login) {
        return userRepository.existsByLoginIgnoreCase(login.trim());
    }

    @Transactional
    public User register(String login, String password) {
        return userRepository.save(new User(login.trim(), passwordHasher.hash(password)));
    }

    @Transactional(readOnly = true)
    public Optional<User> authenticate(String login, String password) {
        return userRepository.findByLoginIgnoreCase(login.trim())
                .filter(user -> passwordHasher.matches(password, user.getPassword()));
    }
}
