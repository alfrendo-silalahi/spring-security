package dev.alfrendosilalahi.spring.security.dto.error;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationEntryPointFailedResponse {

    private int status;

    private String error;

    private String message;

}
