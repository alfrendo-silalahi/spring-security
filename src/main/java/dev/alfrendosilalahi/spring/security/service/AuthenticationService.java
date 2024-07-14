package dev.alfrendosilalahi.spring.security.service;

import dev.alfrendosilalahi.spring.security.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.alfrendosilalahi.spring.security.config.JwtService;
import dev.alfrendosilalahi.spring.security.dto.AuthenticationResponseDTO;
import dev.alfrendosilalahi.spring.security.dto.LoginRequestDTO;
import dev.alfrendosilalahi.spring.security.dto.RegisterRequestDTO;
import dev.alfrendosilalahi.spring.security.entity.User;
import dev.alfrendosilalahi.spring.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        User user = User.builder()
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .build();
        User newUser = userRepository.save(user);
        String token = jwtService.generateToken(newUser);
        return AuthenticationResponseDTO.builder().token(token).build();
    }

    public AuthenticationResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> {
                    ResourceNotFoundException exception = new ResourceNotFoundException("user", "email", loginRequestDTO.getEmail());
                    log.warn(exception.getMessage());
                    return exception;
                });
        String token = jwtService.generateToken(user);
        return AuthenticationResponseDTO.builder().token(token).build();
    }

}
