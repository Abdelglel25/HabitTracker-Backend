package com.sa.habittrackerbackend.conteoller.auth;

import com.sa.habittrackerbackend.dto.user.LoginUserDto;
import com.sa.habittrackerbackend.dto.user.RegisterUserDto;
import com.sa.habittrackerbackend.dto.user.UserDto;
import com.sa.habittrackerbackend.dto.user.UserLoginResponseDto;
import com.sa.habittrackerbackend.service.authentication.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationControllerImpl implements AuthenticationController{
    private final AuthService authService;

    @Autowired
    public AuthenticationControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(authService.signUp(registerUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid LoginUserDto loginUserDto) {
        return ResponseEntity.ok(authService.login(loginUserDto));
    }
}
