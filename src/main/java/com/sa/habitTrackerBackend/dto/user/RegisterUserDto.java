package com.sa.habitTrackerBackend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserDto {
    @NotBlank(message = "Missing field: firstname")
    @Size(max = 30, min = 3, message = "Invalid field: firstName must have at least 3 characters and maximum of 30 characters")
    private String firstName;

    @NotBlank(message = "Missing field: lastname")
    @Size(max = 30, min = 3, message = "Invalid field: lastname must have at least 3 characters and maximum of 30 characters")
    private String lastName;

    @Email(message = "Invalid field: email")
    @NotBlank(message = "Missing field: email")
    @Size(min = 6, max = 100, message = "Invalid field: email must have 255 chars max")
    private String email;

    @NotBlank(message = "Missing field: password")
    @Size(min = 4, max = 255, message = "Invalid field: password must contain between 8 and 255 chars")
    private String password;
}
