package com.example.bankcards.controller;

import com.example.bankcards.security.JwtTokenProvider;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUser_Success() {
        // Setup test data
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        when(tokenProvider.generateToken(any())).thenReturn("mockToken");

        // Execute
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Verify
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void registerUser_Success() {
        // Setup test data
        AuthController.SignUpRequest signUpRequest = new AuthController.SignUpRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setPassword("password");
        signUpRequest.setRole("USER");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Execute
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Verify
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameTaken() {
        // Setup test data
        AuthController.SignUpRequest signUpRequest = new AuthController.SignUpRequest();
        signUpRequest.setUsername("existinguser");
        signUpRequest.setPassword("password");
        signUpRequest.setRole("USER");

        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // Execute
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Verify
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        verify(userRepository, never()).save(any(User.class));
    }
}