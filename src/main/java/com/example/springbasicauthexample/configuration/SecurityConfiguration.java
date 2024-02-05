package com.example.springbasicauthexample.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    @ConditionalOnProperty(name = "app.security.type", havingValue = "db")
    public PasswordEncoder passwordEncoder() {

        return new  BCryptPasswordEncoder(12);
    }

    @Bean
    @ConditionalOnProperty(name = "app.security.type", havingValue = "db")
    public AuthenticationManager dbAuthenticationManager(HttpSecurity security,
                                                         UserDetailsService service,
                                                         PasswordEncoder encoder) throws Exception {

        var authManagerBuilder = security.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(service);
        var authProvider = new DaoAuthenticationProvider(encoder);
        authProvider.setUserDetailsService(service);
        authManagerBuilder.authenticationProvider(authProvider);

        return authManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, AuthenticationManager manager) {

        try {
            security.authorizeHttpRequests((auth) -> auth.requestMatchers("/api/v1/user/**")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/api/v1/admin/**")
                    .hasAnyRole("ADMIN")
                    .requestMatchers("/api/v1/public/**")
                    .permitAll()
                    .anyRequest().authenticated())
                    .csrf(AbstractHttpConfigurer::disable)
                    .httpBasic(Customizer.withDefaults())
                    .sessionManagement(httpSecuritySessionManagementConfigurer ->
                            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationManager(manager);

            return security.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 }
