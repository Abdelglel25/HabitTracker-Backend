package com.sa.habitTrackerBackend.service.authentication;

import com.sa.habitTrackerBackend.dto.user.LoginUserDto;
import com.sa.habitTrackerBackend.dto.user.RegisterUserDto;
import com.sa.habitTrackerBackend.dto.user.UserDto;
import com.sa.habitTrackerBackend.dto.user.UserLoginResponseDto;

public interface AuthService {

    UserDto signUp(RegisterUserDto registerUserDto);

    UserLoginResponseDto login(LoginUserDto loginUserDto);
}
