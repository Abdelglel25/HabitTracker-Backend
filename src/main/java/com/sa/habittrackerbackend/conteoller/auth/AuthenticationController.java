package com.sa.habittrackerbackend.conteoller.auth;

import com.sa.habittrackerbackend.dto.auth.ApiErrorDto;
import com.sa.habittrackerbackend.dto.user.LoginUserDto;
import com.sa.habittrackerbackend.dto.user.RegisterUserDto;
import com.sa.habittrackerbackend.dto.user.UserDto;
import com.sa.habittrackerbackend.dto.user.UserLoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Authentication", description = "Endpoints for authentication and authorization operations")
public interface AuthenticationController {
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    ResponseEntity<UserDto> register(@RequestBody @Valid RegisterUserDto registerUserDto);

    @Operation(summary = "Sign in as a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "User or password is incorrect",
                    content = @Content(schema = @Schema(implementation = ApiErrorDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiErrorDto.class)))
    })
    ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid LoginUserDto loginUserDto);
}
