package com.sa.habitTrackerBackend.utils.entity;

import com.sa.habitTrackerBackend.dao.UserEntity;
import com.sa.habitTrackerBackend.utils.enums.RolesEnum;

import java.util.ArrayList;
import java.util.List;

public class UserTestUtility {

    public static UserEntity createSingleUserWithRole(RolesEnum rolesEnum) {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("userSing@email.com");
        user.setFirstName("userSingle");
        user.setLastName("lastNameSing");
        user.setPassword(user.getLastName());
        user.setRole(rolesEnum);
        return user;
    }

    public static List<UserEntity> buildUsersWithUSERRole(int numberOfUsers) {
        if (numberOfUsers < 1)
            numberOfUsers = 1;

        List<UserEntity> users = new ArrayList<>();
        for (int i = 0; i < numberOfUsers; i++) {
            UserEntity user = new UserEntity();
            user.setId(1L);
            user.setEmail("user%s@email.com".formatted(i));
            user.setFirstName("user%s".formatted(i));
            user.setLastName("lastName%s".formatted(i));
            user.setPassword(user.getLastName());
            users.add(user);
        }
        return users;
    }
}
