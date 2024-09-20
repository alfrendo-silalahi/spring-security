package dev.alfrendosilalahi.spring.security.dto.error;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseErrorResponse {

    private int code;

    private String message;

}
