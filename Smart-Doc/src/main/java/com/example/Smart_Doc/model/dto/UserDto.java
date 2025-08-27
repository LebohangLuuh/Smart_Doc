package com.example.Smart_Doc.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    private String profilePictureUrl;
    private String address;
    private String resetToken;
    private String resetPassword;
    private String gender;
    private String nationality;
    private String dateOfBirth;

    // doctor
    private String bio;
    private Integer yearsOfExperience;
    private String qualifications;
    private String specialization;

    // patient
    private String emergencyContactName;
    private String emergencyContactPhone;

}
