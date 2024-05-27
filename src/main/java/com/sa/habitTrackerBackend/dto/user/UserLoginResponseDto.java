package com.sa.habitTrackerBackend.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDto {
    private String token;

    private long expiresIn;
}
