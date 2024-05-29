package com.sa.habitTrackerBackend.configurations.auth;

import com.sa.habitTrackerBackend.dao.UserEntity;
import com.sa.habitTrackerBackend.utils.entity.UserTestUtility;
import com.sa.habitTrackerBackend.utils.enums.RolesEnum;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUserDetailsTest {

    @Test
    void testEquals_sameObjects() {
        UserEntity userEntity = UserTestUtility.createSingleUserWithRole(RolesEnum.USER);
        var userDetails1 = new CustomUserDetails(userEntity, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        var userDetails2 = userDetails1;

        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void testEquals_sameUserEntity() {
        UserEntity userEntity = UserTestUtility.createSingleUserWithRole(RolesEnum.USER);
        var userDetails1 = new CustomUserDetails(userEntity, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        var userDetails2 = new CustomUserDetails(userEntity, Collections.singletonList(new SimpleGrantedAuthority("USER")));

        assertEquals(userDetails1, userDetails2);
    }

    @Test
    void testEquals_differentUserEntity() {
        List<UserEntity> userEntityList = UserTestUtility.buildUsersWithUSERRole(2);
        UserEntity userEntity1 = userEntityList.get(0);
        UserEntity userEntity2 = userEntityList.get(1);
        var userDetails1 = new CustomUserDetails(userEntity1, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        var userDetails2 = new CustomUserDetails(userEntity2, Collections.singletonList(new SimpleGrantedAuthority("USER")));

        assertNotEquals(userDetails1, userDetails2);
    }

    @Test
    void testEquals_differentObject() {
        UserEntity userEntity = UserTestUtility.createSingleUserWithRole(RolesEnum.USER);
        var userDetails = new CustomUserDetails(userEntity, Collections.singletonList(new SimpleGrantedAuthority("USER")));
        var notUserDetailsObj = new Object();

        assertNotEquals(userDetails, notUserDetailsObj);
    }
}