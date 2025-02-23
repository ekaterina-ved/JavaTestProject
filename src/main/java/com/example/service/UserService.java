package com.example.service;

import com.example.dto.UserDto;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // В реальном приложении пароль нужно хешировать

        return userRepository.save(user);
    }
} 