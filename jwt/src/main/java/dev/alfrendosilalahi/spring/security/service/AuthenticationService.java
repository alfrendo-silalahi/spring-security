package dev.alfrendosilalahi.spring.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alfrendosilalahi.spring.security.dto.AuthenticationResponseDTO;
import dev.alfrendosilalahi.spring.security.dto.LoginRequestDTO;
import dev.alfrendosilalahi.spring.security.dto.RegisterRequestDto;
import dev.alfrendosilalahi.spring.security.dto.request.InitForgetPasswordRequestDTO;
import dev.alfrendosilalahi.spring.security.dto.response.InitForgetPasswordResponseDTO;
import dev.alfrendosilalahi.spring.security.entity.User;
import dev.alfrendosilalahi.spring.security.exception.ResourceNotFoundException;
import dev.alfrendosilalahi.spring.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final RedisService redisService;

    private final ObjectMapper objectMapper;

    @Transactional
    public AuthenticationResponseDTO register(RegisterRequestDto registerRequestDto) {
        User user = User.builder()
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(registerRequestDto.getRole())
                .build();
        User newUser = userRepository.save(user);
        String token = jwtService.generateToken(newUser);
        String refreshToken = jwtService.generateRefreshToken(newUser);
        return AuthenticationResponseDTO.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponseDTO login(LoginRequestDTO loginRequestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        User user = findUserByEmail(loginRequestDto.getEmail());
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponseDTO.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public InitForgetPasswordResponseDTO initForgetPassword(InitForgetPasswordRequestDTO requestDTO) {
        User user = findUserByEmail(requestDTO.email());
        String key = String.format("INIT_FORGET_PASSWORD:%s", UUID.randomUUID());
        redisService.putValue(key, user);
        return null;
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    ResourceNotFoundException exception = new ResourceNotFoundException("user", "email", email);
                    log.error(exception.getMessage());
                    return exception;
                });
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                AuthenticationResponseDTO authResponse = AuthenticationResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(authResponse));
            }
        }
    }

}
