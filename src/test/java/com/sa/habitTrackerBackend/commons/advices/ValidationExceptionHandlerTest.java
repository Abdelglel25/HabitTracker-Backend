package com.sa.habitTrackerBackend.commons.advices;

import com.sa.habitTrackerBackend.dto.user.LoginUserDto;
import com.sa.habitTrackerBackend.utils.AbstractIntegrationIT;
import com.sa.habitTrackerBackend.utils.TestObjectMapper.TestObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ValidationExceptionHandlerTest extends AbstractIntegrationIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestObjectMapper objectMapper;

    @Test
    public void testHandleMethodArgumentNotValid() throws Exception {
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .email("wrongEmailFormat")
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