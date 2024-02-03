package com.example.springbasicauthexample.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.MessageFormat;
import java.util.stream.Collectors;


@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasAnyAuthority(ROLE_ADMIN)")
    public ResponseEntity<String> userGet(@AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                MessageFormat.format("Method called by admin: {0}, Role is: {1}",
                        userDetails.getUsername(),
                        userDetails.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(",")))

        );
    }
}
