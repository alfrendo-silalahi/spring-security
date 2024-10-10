package dev.alfrendosilalahi.spring.security.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitForgetPasswordResponseDTO {

    private String redisKey;

}
