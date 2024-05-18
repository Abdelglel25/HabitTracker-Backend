package com.sa.habittrackerbackend.configurations.auth;

import com.sa.habittrackerbackend.dao.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {
    private final UserEntity userEntity;

    public CustomUserDetails(UserEntity user , Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.userEntity = user;
    }
}
