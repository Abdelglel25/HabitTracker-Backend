package com.sa.habitTrackerBackend.authentication;


import com.sa.habitTrackerBackend.dao.UserEntity;
import com.sa.habitTrackerBackend.dto.user.LoginUserDto;
import com.sa.habitTrackerBackend.dto.user.UserLoginResponseDto;
import com.sa.habitTrackerBackend.repository.UserRepository;
import com.sa.habitTrackerBackend.utils.AbstractIntegrationIT;
import com.sa.habitTrackerBackend.utils.TestObjectMapper.TestObjectMapper;
import com.sa.habitTrackerBackend.utils.entity.UserTestUtility;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthenticationControllerIT extends AbstractIntegrationIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;  // should be replaced by DBUnit insertion

    @Value("${security.jwt.expiration-time}")
    private Long expirationTime;

    @Nested
    class Login {
        @Test
        void loginUnauthorized() throws Exception {
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .email("habittracker@gmail.com")
                    .password("password")
                    .build();

            String loginRequest = objectMapper.toJson(loginUserDto);

            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginRequest)
            ).andExpect(status().isUnauthorized());
        }

        @Test
        void loginSuccessfully() throws Exception {
            UserEntity userEntity = (UserEntity) UserTestUtility.buildUsersWithUSERRole(1);
            String decodedPassword = userEntity.getPassword();
            userEntity.setPassword(passwordEncoder.encode(decodedPassword));

            userRepository.save(userEntity);

            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .email(userEntity.getEmail())
                    .password(decodedPassword)
                    .build();

            String loginRequest = objectMapper.toJson(loginUserDto);

            MvcResult mvcResult =  mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest)
                ).andExpect(status().isOk())
                    .andReturn();

            String response = mvcResult.getResponse().getContentAsString();

            UserLoginResponseDto loginResponseDto = objectMapper.toObject(response, UserLoginResponseDto.class);

            assertEquals(expirationTime, loginResponseDto.getExpiresIn());
            assertTrue(loginResponseDto.getToken().matches("(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)"));
        }
    }


}
