package com.sa.habittrackerbackend.configurations.auth;

import com.sa.habittrackerbackend.dao.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;

@Getter
public class CustomUserDetails extends User {
    private transient final UserEntity userEntity;

    public CustomUserDetails(UserEntity user , Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.userEntity = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomUserDetails that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(userEntity, that.userEntity);
    }
}
