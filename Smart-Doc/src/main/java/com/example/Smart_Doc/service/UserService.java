package com.example.Smart_Doc.service;

import com.example.Smart_Doc.exception.InvalidCredentialsException;
import com.example.Smart_Doc.exception.UserAlreadyExistsException;
import com.example.Smart_Doc.model.dto.LoginRequest;
import com.example.Smart_Doc.model.dto.RegisterRequest;
import com.example.Smart_Doc.model.dto.UserResponse;
import com.example.Smart_Doc.model.entity.User;
import com.example.Smart_Doc.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
@Transactional
public class UserService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public Optional<User> getUserEntityByEmail(String email) {
                return userRepository.findByEmail(email.toLowerCase());
        }

        public UserResponse registerUser(RegisterRequest registerRequest) {
                // Validate passwords match
                if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                        throw new IllegalArgumentException("Passwords do not match");
                }

                // Normalize email
                String normalizedEmail = registerRequest.getEmail().toLowerCase();

                // Check if user already exists
                if (userRepository.existsByEmail(normalizedEmail)) {
                        throw new UserAlreadyExistsException("User with email " + normalizedEmail + " already exists");
                }

                // Validate doctor-specific requirements
                if ("doctor".equalsIgnoreCase(registerRequest.getRole())) {
                        if (registerRequest.getPracticeNumber() == null || registerRequest.getPracticeNumber().trim().isEmpty()) {
                                throw new IllegalArgumentException("Practice number is required for doctors");
                        }

                        if (userRepository.existsByPracticeNumber(registerRequest.getPracticeNumber())) {
                                throw new UserAlreadyExistsException("Practice number already exists");
                        }
                }

                // Create new user
                User user = new User();
                user.setFullname(registerRequest.getFullname());
                user.setEmail(normalizedEmail);
                user.setRole(registerRequest.getRole().toLowerCase());
                user.setPracticeNumber(registerRequest.getPracticeNumber());
                user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

                User savedUser = userRepository.save(user);
                return new UserResponse(savedUser);
        }

        public UserResponse loginUser(LoginRequest loginRequest) {
                String normalizedEmail = loginRequest.getEmail().toLowerCase();
                Optional<User> optionalUser = userRepository.findByEmail(normalizedEmail);

                if (optionalUser.isEmpty()) {
                        throw new InvalidCredentialsException("Invalid email or password");
                }

                User user = optionalUser.get();

                if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        throw new InvalidCredentialsException("Invalid email or password");
                }

                return new UserResponse(user);
        }

        public Optional<UserResponse> getUserByEmail(String email) {
                return userRepository.findByEmail(email.toLowerCase())
                        .map(UserResponse::new);
        }

        public Optional<UserResponse> getUserById(Integer id) {
                return userRepository.findById(id)
                        .map(UserResponse::new);
        }
}