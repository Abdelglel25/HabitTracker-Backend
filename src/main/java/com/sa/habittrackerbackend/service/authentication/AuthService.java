package com.sa.habittrackerbackend.service.authentication;

import com.sa.habittrackerbackend.dto.user.LoginUserDto;
import com.sa.habittrackerbackend.dto.user.RegisterUserDto;
import com.sa.habittrackerbackend.dto.user.UserDto;
import com.sa.habittrackerbackend.dto.user.UserLoginResponseDto;

public interface AuthService {

    UserDto signUp(RegisterUserDto registerUserDto);

    UserLoginResponseDto login(LoginUserDto loginUserDto);
}
