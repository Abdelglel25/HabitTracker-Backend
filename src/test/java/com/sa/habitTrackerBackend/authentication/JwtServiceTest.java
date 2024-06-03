package com.sa.habitTrackerBackend.authentication;

import com.sa.habitTrackerBackend.configurations.auth.CustomUserDetails;
import com.sa.habitTrackerBackend.configurations.auth.JwtService;
import com.sa.habitTrackerBackend.dao.UserEntity;
import com.sa.habitTrackerBackend.repository.UserRepository;
import com.sa.habitTrackerBackend.utils.AbstractIntegrationIT;
import com.sa.habitTrackerBackend.utils.entity.UserTestUtility;
import com.sa.habitTrackerBackend.utils.enums.RolesEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest extends AbstractIntegrationIT {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Value("${security.jwt.expiration-time}")
    private Long expirationTime;

    @Test
    void generateTokenSuccessfully() {
        UserEntity user = UserTestUtility.createSingleUserWithRole(RolesEnum.USER);
        userRepository.save(user);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        UserDetails customUserDetails = new CustomUserDetails(user, authorities);
        // openssl rand -base64 32
        String token = jwtService.generateToken(customUserDetails);
        assertTrue(token.matches("(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)"));
    }

    @Test
    void generateTokenFail() {
        UserEntity user = UserTestUtility.createSingleUserWithRole(RolesEnum.USER);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        UserDetails customUserDetails = new CustomUserDetails(user, authorities);
        // openssl rand -base64 32
        assertThrows(UsernameNotFoundException.class, () -> jwtService.generateToken(customUserDetails));
    }

    @Test
    void extractClaimsSuccessfully() {
        UserEntity user = UserTestUtility.createSingleUserWithRole(RolesEnum.USER);
        userRepository.save(user);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        UserDetails customUserDetails = new CustomUserDetails(user, authorities);
        String token = jwtService.generateToken(customUserDetails);

        assertEquals(user.getEmail(), jwtService.extractClaim(token, Claims::getSubject));

        Claims claims = jwtService.extractAllClaims(token);

        assertEquals(user.getRole().toString(), claims.get("role", String.class));

        Date expiration = claims.getExpiration();

        assertTrue(expiration.before(new Date(System.currentTimeMillis() + expirationTime)));

        assertTrue(jwtService.isTokenValid(token, customUserDetails));
    }

    @Test
    @DirtiesContext
    void tokenExpired() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        expirationTime = 4L;
        Field privateField = JwtService.class.getDeclaredField("jwtExpiration");
        privateField.setAccessible(true);
        privateField.set(jwtService, expirationTime);

        UserEntity user = UserTestUtility.createSingleUserWithRole(RolesEnum.USER);
        userRepository.save(user);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        UserDetails customUserDetails = new CustomUserDetails(user, authorities);
        String token = jwtService.generateToken(customUserDetails);

        Thread.sleep(4L);
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, customUserDetails));
    }


}
