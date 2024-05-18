package com.sa.habittrackerbackend.service.user;

import com.sa.habittrackerbackend.dao.UserEntity;

public interface UserService {
    UserEntity getUserByEmail(String email);
}
