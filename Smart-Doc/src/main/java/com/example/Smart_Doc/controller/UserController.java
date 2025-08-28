package com.example.Smart_Doc.controller;

import com.example.Smart_Doc.model.dto.LoginRequestDto;
import com.example.Smart_Doc.model.dto.UpdateUserRequestDto;
import com.example.Smart_Doc.model.dto.UserDto;
import com.example.Smart_Doc.model.dto.UserRequestDto;
import com.example.Smart_Doc.service.UserService;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Getter
@Setter
@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            UserDto registeredUser = userService.registerUser(userRequestDto);
            return ResponseEntity.ok(new ApiResponse<>(200, "User registered successfully", registeredUser, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage(), null, false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            UserDto user = userService.loginUser(loginRequest);
            return ResponseEntity.ok(new ApiResponse<>(200, "Login successful", user, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, e.getMessage(), null, false));
        }
    }

    @GetMapping("/getUserByEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        try {
            // You'll need to implement this in your UserService
            UserDto user = userService.getUserDtoByEmail(email);
            return ResponseEntity.ok(new ApiResponse<>(200, "User found", user, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage(), null, false));
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        try {
            UserDto updatedUser = userService.updateUserDetails(updateUserRequestDto);
            return ResponseEntity.ok(new ApiResponse<>(200, "User updated successfully", updatedUser, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage(), null, false));
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            userService.forgotPassword(email);
            return ResponseEntity.ok(new ApiResponse<>(200, "Password reset instructions sent to your email", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage(), null, false));
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            userService.resetPassword(newPassword, token);
            return ResponseEntity.ok(new ApiResponse<>(200, "Password reset successfully", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage(), null, false));
        }
    }

    // Inner class for consistent API responses
    public static class ApiResponse<T> {
        private int status_code;
        private String message;
        private T data;
        private boolean success;

        public ApiResponse(int status_code, String message, T data, boolean success) {
            this.status_code = status_code;
            this.message = message;
            this.data = data;
            this.success = success;
        }
    }
}