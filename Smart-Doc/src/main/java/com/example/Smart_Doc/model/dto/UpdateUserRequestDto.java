package com.example.Smart_Doc.model.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    private String address;
    private String profilePictureUrl;
    private String gender;
    private String nationality;
    private String dateOfBirth;

    // Doctor specific updates
    private String bio;
    private Integer yearsOfExperience;
    private String qualifications;
    private String specialization;
    private Double consultationFee;

    // Patient specific updates
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String medicalHistory;
    private String allergies;
}