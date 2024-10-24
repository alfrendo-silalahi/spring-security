package dev.alfrendosilalahi.spring.security.controller;

import dev.alfrendosilalahi.spring.security.dto.AuthenticationResponseDTO;
import dev.alfrendosilalahi.spring.security.dto.LoginRequestDTO;
import dev.alfrendosilalahi.spring.security.dto.RegisterRequestDto;
import dev.alfrendosilalahi.spring.security.dto.request.InitForgetPasswordRequestDTO;
import dev.alfrendosilalahi.spring.security.dto.response.InitForgetPasswordResponseDTO;
import dev.alfrendosilalahi.spring.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDto registerRequestDto) {
        var response = authenticationService.register(registerRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody LoginRequestDTO loginRequestDTO) {
        var response = authenticationService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<InitForgetPasswordResponseDTO> initForgetPassword(@RequestBody InitForgetPasswordRequestDTO requestDTO) {
        var response = authenticationService.initForgetPassword(requestDTO);
        return ResponseEntity.ok(response);
    }

}
