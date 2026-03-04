package com.R8.BloggingPlatform.controllers;

import com.R8.BloggingPlatform.domain.dtos.AuthResponse;
import com.R8.BloggingPlatform.domain.dtos.LoginRequest;
import com.R8.BloggingPlatform.domain.entites.User;
import com.R8.BloggingPlatform.services.AuthenticationService;
import com.R8.BloggingPlatform.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth/login")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        String tokenValue = authenticationService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/createNewUser")
    public ResponseEntity<?> createNewUser(@RequestBody User newUser) {
        System.out.println("In Controller");
        System.out.println("Request Body:"+newUser);
        User createdUser = userService.createNewUser(newUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
