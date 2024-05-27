package com.sa.habitTrackerBackend.service.authentication;

import com.sa.habitTrackerBackend.configurations.auth.CustomUserDetails;
import com.sa.habitTrackerBackend.configurations.auth.JwtService;
import com.sa.habitTrackerBackend.dao.UserEntity;
import com.sa.habitTrackerBackend.dto.user.LoginUserDto;
import com.sa.habitTrackerBackend.dto.user.RegisterUserDto;
import com.sa.habitTrackerBackend.dto.user.UserDto;
import com.sa.habitTrackerBackend.dto.user.UserLoginResponseDto;
import com.sa.habitTrackerBackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        performAuthentication(loginUserDto);

        UserEntity userEntity = findUserByEmail(loginUserDto.getEmail());

        return buildLoginResponse(userEntity);
    }

    private void performAuthentication(LoginUserDto loginUserDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed: {}", e.getMessage());
            throw new BadCredentialsException("Bad credentials");
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new InternalAuthenticationServiceException("Authentication failed");
        }
    }

    private UserEntity findUserByEmail(String email) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isEmpty()) {
            log.error("User not found");
            throw new InternalError("Iternal server error");
        }
        return userEntity.get();
    }

    private UserLoginResponseDto buildLoginResponse(UserEntity userEntity) {
        return UserLoginResponseDto.builder()
                .token(jwtService.generateToken(new CustomUserDetails(userEntity, List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole())))))
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }
}
