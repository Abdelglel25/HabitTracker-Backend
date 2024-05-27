package com.sa.habittrackerbackend.service.authentication;

import com.sa.habittrackerbackend.configurations.auth.CustomUserDetails;
import com.sa.habittrackerbackend.configurations.auth.JwtService;
import com.sa.habittrackerbackend.dao.UserEntity;
import com.sa.habittrackerbackend.dto.user.LoginUserDto;
import com.sa.habittrackerbackend.dto.user.RegisterUserDto;
import com.sa.habittrackerbackend.dto.user.UserDto;
import com.sa.habittrackerbackend.dto.user.UserLoginResponseDto;
import com.sa.habittrackerbackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @Override
    public UserDto signUp(RegisterUserDto registerUserDto) {
        UserEntity userEntity = new UserEntity();

        userEntity.setFirstName(registerUserDto.getFirstName());
        userEntity.setLastName(registerUserDto.getLastName());
        userEntity.setEmail(registerUserDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        UserEntity savedUser = userRepository.save(userEntity);

        return UserDto.builder()
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    public UserLoginResponseDto login(LoginUserDto loginUserDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
            );
        } catch (Exception e) {
            log.warn("Authentication failed: {} :: With email: {}", e.getMessage(), loginUserDto.getEmail());
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        UserEntity userEntity = userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserLoginResponseDto.builder()
                .token(jwtService.generateToken(new CustomUserDetails(userEntity, List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole())))))
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }
}
