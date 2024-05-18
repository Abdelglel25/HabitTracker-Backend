package com.sa.habittrackerbackend.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDto {
    private String token;

    private long expiresIn;
}
