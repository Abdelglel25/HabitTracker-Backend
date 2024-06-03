package com.sa.habitTrackerBackend.authentication;


import com.sa.habitTrackerBackend.dao.UserEntity;
import com.sa.habitTrackerBackend.dto.user.LoginUserDto;
import com.sa.habitTrackerBackend.dto.user.RegisterUserDto;
import com.sa.habitTrackerBackend.dto.user.UserLoginResponseDto;
import com.sa.habitTrackerBackend.repository.UserRepository;
import com.sa.habitTrackerBackend.utils.AbstractIntegrationIT;
import com.sa.habitTrackerBackend.utils.TestObjectMapper.TestObjectMapper;
import com.sa.habitTrackerBackend.utils.entity.UserTestUtility;
import com.sa.habitTrackerBackend.utils.enums.RolesEnum;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
            UserEntity userEntity = UserTestUtility.createSingleUserWithRole(RolesEnum.USER);
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

            assertEquals(expirationTime, loginResponseDto.getExpireDurationMillis());
            assertTrue(loginResponseDto.getToken().matches("(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)"));
        }

        @Test
        void loginWithInvalidFields() throws Exception {
            LoginUserDto loginUserDto = LoginUserDto.builder()
                    .email("InvalidEmailFormat")
                    .password("short")
                    .build();

            String loginRequest = objectMapper.toJson(loginUserDto);

            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginRequest)
            ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("There are invalid or missing fields"))
                    .andExpect(jsonPath("$.errors",
                            containsInAnyOrder("Invalid field: password must have between 6 and 255 characters",
                                    "Invalid filed: email is not valid"
                            )));
        }
    }

    @Nested
    class Signup {
        @Test
        void signupSuccessfully() throws Exception {
            RegisterUserDto signupUserDto = RegisterUserDto.builder()
                    .firstName("firstNameTest")
                    .lastName("lastNameTest")
                    .email("testEmail@gmail.com")
                    .password("testPassword")
                    .build();

            String signupRequest = objectMapper.toJson(signupUserDto);

            mockMvc.perform(post("/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(signupRequest)
            ).andExpect(status().isCreated());
        }

        @Test
        void signupWithInvalidFields() throws Exception {
            RegisterUserDto signupUserDto = RegisterUserDto.builder()
                    .firstName("nn")
                    .lastName("ThisIsLastNameThatExceed30Chars")
                    .email("InvalidEmailFormat")
                    .password("testPassword")
                    .build();

            String signupRequest = objectMapper.toJson(signupUserDto);

            mockMvc.perform(post("/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(signupRequest)
            ).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("There are invalid or missing fields"))
                    .andExpect(jsonPath("$.errors",
                            containsInAnyOrder(
                                    "Invalid field: email",
                                    "Invalid field: lastname must have at least 3 characters and maximum of 30 characters",
                                    "Invalid field: firstName must have at least 3 characters and maximum of 30 characters"
                            )));
        }
    }

}
