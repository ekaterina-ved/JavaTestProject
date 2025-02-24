package com.example.service;

import com.example.dto.UserDto;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
    void createUser_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User createdUser = userService.createUser(userDto);

        // Then
        assertNotNull(createdUser);
        assertEquals(userDto.getUsername(), createdUser.getUsername());
        assertEquals(userDto.getEmail(), createdUser.getEmail());
        verify(userRepository).existsByEmail(userDto.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WithExistingEmail_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Пользователь с таким email уже существует", exception.getMessage());
        verify(userRepository).existsByEmail(userDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_WithInvalidEmail_ThrowsException() {
        // Given
        userDto.setEmail("invalid-email");

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Некорректный формат email", exception.getMessage());
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithEmptyEmail_ThrowsException() {
        // Given
        userDto.setEmail("");

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Email не может быть пустым", exception.getMessage());
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_WithNullEmail_ThrowsException() {
        // Given
        userDto.setEmail(null);

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(userDto);
        });

        assertEquals("Email не может быть пустым", exception.getMessage());
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }
} 