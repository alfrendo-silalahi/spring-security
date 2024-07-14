package dev.alfrendosilalahi.spring.security.controller;

import dev.alfrendosilalahi.spring.security.dto.AuthenticationResponseDTO;
import dev.alfrendosilalahi.spring.security.dto.LoginRequestDTO;
import dev.alfrendosilalahi.spring.security.dto.RegisterRequestDTO;
import dev.alfrendosilalahi.spring.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody RegisterRequestDTO registerRequestDTO) {
        return new ResponseEntity<>(authenticationService.register(registerRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> register(
            @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authenticationService.login(loginRequestDTO));
    }

}
