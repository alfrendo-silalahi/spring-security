package dev.alfrendosilalahi.spring.security.dto.error;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessDeniedResponse {

    private int status;

    private String error;

    private String message;

    private String path;

    private LocalDateTime timestamp;

}
