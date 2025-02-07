package dev.alfrendosilalahi.spring.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alfrendosilalahi.spring.security.dto.error.AccessDeniedResponse;
import dev.alfrendosilalahi.spring.security.dto.error.AuthenticationEntryPointFailedResponse;
import dev.alfrendosilalahi.spring.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

import static dev.alfrendosilalahi.spring.security.entity.Permission.*;
import static dev.alfrendosilalahi.spring.security.entity.Role.ADMIN;
import static dev.alfrendosilalahi.spring.security.entity.Role.MANAGEMENT;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // used for annotation based authorization
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    private final ObjectMapper objectMapper;

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
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            AuthenticationEntryPointFailedResponse failedResponse = AuthenticationEntryPointFailedResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .message(authException.getMessage())
                    .build();

            response.getWriter().write(objectMapper.writeValueAsString(failedResponse));
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            log.warn(accessDeniedException.getMessage());

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            AccessDeniedResponse accessDeniedResponse = AccessDeniedResponse.builder()
                    .status(HttpStatus.FORBIDDEN.value())
                    .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                    .message(accessDeniedException.getMessage())
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now())
                    .build();

            response.getWriter().write(objectMapper.writeValueAsString(accessDeniedResponse));
        };
    }

}
