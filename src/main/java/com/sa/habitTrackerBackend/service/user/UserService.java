package com.sa.habitTrackerBackend.service.user;

import com.sa.habitTrackerBackend.dao.UserEntity;

public interface UserService {
    UserEntity getUserByEmail(String email);
}
