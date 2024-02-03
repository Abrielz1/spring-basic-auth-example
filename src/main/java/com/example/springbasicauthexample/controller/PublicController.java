package com.example.springbasicauthexample.controller;

import com.example.springbasicauthexample.dto.UserDto;
import com.example.springbasicauthexample.entity.RoleType;
import com.example.springbasicauthexample.entity.User;
import com.example.springbasicauthexample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static com.example.springbasicauthexample.entity.Role.from;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<String> getPublic() {

        return ResponseEntity.ok("Called Public Method");
    }

    @PostMapping("/account")
    public ResponseEntity<UserDto> createUserAcolount(@RequestBody UserDto userDto, @RequestParam RoleType type) {

        return ResponseEntity.status(HttpStatus.CREATED).body(createAccount(userDto, type));
    }

    private UserDto createAccount(UserDto userDto, RoleType type) {

        var newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(userDto.getPassword());
        var createdUser = service.createNewAccount(newUser, from(type));

        return UserDto.builder()
                .username(createdUser.getUsername())
                .password(createdUser.getPassword())
                .build();
    }
}
