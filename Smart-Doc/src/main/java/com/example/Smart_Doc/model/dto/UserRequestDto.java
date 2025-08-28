package com.example.Smart_Doc.model.dto;

import com.example.Smart_Doc.model.enums.UserRole;
import com.example.Smart_Doc.validation.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class UserRequestDto {

    @NotBlank(message = "Full name is required")
    private String fullname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Role is required")
    private String role; // Accept as string from frontend

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    // Optional field for doctors
    private String practiceNumber;

    // Helper methods to split fullname
    @JsonIgnore
    public String getFirstName() {
        if (fullname == null || fullname.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullname.trim().split("\\s+", 2);
        return parts[0];
    }

    @JsonIgnore
    public String getLastName() {
        if (fullname == null || fullname.trim().isEmpty()) {
            return "";
        }
        String[] parts = fullname.trim().split("\\s+", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    @JsonIgnore
    public UserRole getUserRole() {
        if ("doctor".equalsIgnoreCase(role)) {
            return UserRole.DOCTOR;
        } else if ("patient".equalsIgnoreCase(role)) {
            return UserRole.PATIENT;
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }
}