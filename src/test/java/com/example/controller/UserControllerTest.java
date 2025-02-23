package com.example.controller;

import com.example.dto.UserDto;
import com.example.model.User;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
    }

    @Test
    void createUser_Success() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void createUser_WithInvalidEmail_BadRequest() throws Exception {
        userDto.setEmail("invalid-email");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }
} 