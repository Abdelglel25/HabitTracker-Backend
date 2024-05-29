package com.sa.habitTrackerBackend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUserDto {

    @Schema(description = "The User email signed up with",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Size(min = 6, max = 100, message = "Invalid field: email must have between 6 and 100 characters")
    @Email(message = "Invalid filed: email is not valid")
    private String email;

    @Schema(description = "The User password (max 255 chars)",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Size(min = 6, max = 255, message = "Invalid field: password must have between 6 and 255 characters")   // @size for strings, @min and @max is for numerics
    private String password;
}
