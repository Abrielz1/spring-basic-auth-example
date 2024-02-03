package com.example.springbasicauthexample.service;

import com.example.springbasicauthexample.UserRepository;
import com.example.springbasicauthexample.entity.Role;
import com.example.springbasicauthexample.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder encoder;

    public User createNewAccount(User user, Role role) {

        user.setRole(Collections.singletonList(role));
        user.setPassword(user.getPassword());
        return repository.save(user);
    }

    public User findByUserName(String username) {

        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username name was not found"));
    }
}
