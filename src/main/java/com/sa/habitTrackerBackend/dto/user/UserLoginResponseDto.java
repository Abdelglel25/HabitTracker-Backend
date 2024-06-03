package com.sa.habitTrackerBackend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDto {
    private String token;

    @Schema(description = "Expires in milliseconds", example = "1000")
    private long expireDurationMillis;
}
