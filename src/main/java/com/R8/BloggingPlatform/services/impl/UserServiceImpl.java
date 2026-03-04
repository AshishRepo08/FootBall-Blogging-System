package com.R8.BloggingPlatform.services.impl;

import com.R8.BloggingPlatform.domain.entites.User;
import com.R8.BloggingPlatform.repositories.UserRepository;
import com.R8.BloggingPlatform.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: "+id));
    }

    @Override
    public User createNewUser(User newUser) {
        System.out.println("In Service Layer");
        User createdUser = User.builder().email(newUser.getEmail()).name(newUser.getName()).password(passwordEncoder.encode(newUser.getPassword())).build();
        return userRepository.save(createdUser);
    }
}
