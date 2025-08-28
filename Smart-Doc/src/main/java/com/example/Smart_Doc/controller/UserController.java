package com.example.Smart_Doc.controller;

import org.hibernate.jmx.spi.JmxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import com.example.Smart_Doc.model.dto.*;
import com.example.Smart_Doc.service.UserService;

import grails.plugin.springsecurity.rest.JwtService;

import com.example.Smart_Doc.mapper.UserMapper;
import com.example.Smart_Doc.model.entity.User;
import com.example.Smart_Doc.exception.DataNotFoundException;
import com.example.Smart_Doc.exception.AuthenticationException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/profile")
    public ResponseEntity<Result<UserDto>> getUserByEmail(
            @RequestParam @Email String email,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            // Security check: users can only access their own profile
            if (!email.equals(userDetails.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new Result<>(HttpStatus.FORBIDDEN.value(), "Access denied", null, false));
            }
            
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new Result<>(HttpStatus.NOT_FOUND.value(), "User not found", null, false));
            }
            
            UserDto userDto = UserMapper.userEntityToUserDto(user);
            return ResponseEntity.ok(new Result<>(HttpStatus.OK.value(), "User retrieved successfully", userDto, true));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to retrieve user: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Result<UserTokenDto>> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            UserDto userDto = userService.registerUser(userRequestDto);
            
            User user = userService.getUserByEmail(userDto.getEmail());
            String token = jwtService.generateToken(user);
            UserTokenDto userTokenDto = UserMapper.userToUserTokenDTO(user, token);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Result<>(HttpStatus.CREATED.value(), "User registered successfully", userTokenDto, true));
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new Result<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null, false));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Registration failed: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Result<UserTokenDto>> loginUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            UserDto userDto = userService.loginUser(loginRequest);
            
            // Generate JWT token
            User user = userService.getUserByEmail(userDto.getEmail());
            String token = jwtService.generateToken(user);
            UserTokenDto userTokenDto = UserMapper.userToUserTokenDTO(user, token);

            return ResponseEntity.ok(new Result<>(HttpStatus.OK.value(), "Login successful", userTokenDto, true));
            
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Result<>(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials", null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Login failed: " + e.getMessage(), null, false));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Result<UserDto>> updateUser(
            @Valid @RequestBody UpdateUserRequestDto updateUserRequestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            // Security check: users can only update their own profile
            if (!updateUserRequestDto.getEmail().equals(userDetails.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new Result<>(HttpStatus.FORBIDDEN.value(), "Access denied", null, false));
            }

            UserDto userDto = userService.updateUserDetails(updateUserRequestDto);
            return ResponseEntity.ok(new Result<>(HttpStatus.OK.value(), "User updated successfully", userDto, true));

        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Update failed: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Result<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        try {
            userService.forgotPassword(request.getEmail());
            return ResponseEntity.ok(new Result<>(HttpStatus.OK.value(), "Password reset email sent", "Check your email", true));
        } catch (DataNotFoundException e) {
            // Don't reveal if email exists for security
            return ResponseEntity.ok(new Result<>(HttpStatus.OK.value(), "If email exists, reset link will be sent", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to process request: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Result<String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        try {
            userService.resetPassword(request.getNewPassword(), request.getToken());
            return ResponseEntity.ok(new Result<>(HttpStatus.OK.value(), "Password reset successfully", null, true));
        } catch (IllegalArgumentException | DataNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body(new Result<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to reset password: " + e.getMessage(), null, false));
        }
    }
}