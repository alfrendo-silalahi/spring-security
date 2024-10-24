package dev.alfrendosilalahi.spring.security.config;

import dev.alfrendosilalahi.spring.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static dev.alfrendosilalahi.spring.security.entity.Permission.*;
import static dev.alfrendosilalahi.spring.security.entity.Role.ADMIN;
import static dev.alfrendosilalahi.spring.security.entity.Role.MANAGEMENT;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Used for annotation based authorization
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/managements/**").hasAnyRole(ADMIN.name(), MANAGEMENT.name())
                        .requestMatchers(GET, "/api/managements/**").hasAnyAuthority(ADMIN_READ.name())
                        .requestMatchers(POST, "/api/managements/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGEMENT_CREATE.name())
                        .requestMatchers(PUT, "/api/managements/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGEMENT_UPDATE.name())
                        .requestMatchers(DELETE, "/api/managements/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGEMENT_DELETE.name())

                        /*
                        .requestMatchers("/api/admin/**").hasRole(ADMIN.name())
                        .requestMatchers(GET, "/api/admin/**").hasAnyAuthority(ADMIN_READ.name())
                        .requestMatchers(POST, "/api/admin/**").hasAnyAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT, "/api/admin/**").hasAnyAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE, "/api/admin/**").hasAnyAuthority(ADMIN_DELETE.name())
                        */

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
