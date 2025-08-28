package com.example.Smart_Doc.controller;
import com.example.Smart_Doc.model.dto.ApiResponse;
import com.example.Smart_Doc.model.dto.LoginRequest;
import com.example.Smart_Doc.model.dto.RegisterRequest;
import com.example.Smart_Doc.model.dto.UserResponse;
import com.example.Smart_Doc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registerUser")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = userService.registerUser(registerRequest);
        ApiResponse<UserResponse> response = ApiResponse.success("User registered successfully", userResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        UserResponse userResponse = userService.loginUser(loginRequest);
        ApiResponse<UserResponse> response = ApiResponse.success("Login successful", userResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    ApiResponse<UserResponse> response = ApiResponse.success("User found", user);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    ApiResponse<UserResponse> response = ApiResponse.error(404, "User not found");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> {
                    ApiResponse<UserResponse> response = ApiResponse.success("User found", user);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    ApiResponse<UserResponse> response = ApiResponse.error(404, "User not found");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
    }
}