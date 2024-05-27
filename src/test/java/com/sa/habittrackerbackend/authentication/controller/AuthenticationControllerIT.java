package com.sa.habittrackerbackend.authentication.controller;

import com.sa.habittrackerbackend.dto.user.LoginUserDto;
import com.sa.habittrackerbackend.utils.AbstractIntegrationIT;
import com.sa.habittrackerbackend.utils.TestObjectMapper.TestObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthenticationControllerIT extends AbstractIntegrationIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestObjectMapper objectMapper;


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
    }


}
