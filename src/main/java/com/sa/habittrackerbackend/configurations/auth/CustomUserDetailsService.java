package com.sa.habittrackerbackend.configurations.auth;

import com.sa.habittrackerbackend.dao.UserEntity;
import com.sa.habittrackerbackend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user for email {}", username);
        return loadUserByEmail(username);
    }

    private UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUserByEmail(email);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole()));
        return new CustomUserDetails(userEntity, authorities);
    }
}
