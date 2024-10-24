package dev.alfrendosilalahi.spring.security.dto;

import dev.alfrendosilalahi.spring.security.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;

}
