package com.example.Smart_Doc.model.entity;

import com.example.Smart_Doc.model.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

        @Column(nullable = false, name = "first_name")
    private String firstName;

        @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, name = "role")
    private UserRole role;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "address")
    private String address;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_password")
    private String resetPassword;

    @Column(name = "gender")
    private String gender;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    // doctor
    @Column(name = "bio")
    private String bio;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "qualifications")
    private String qualifications;

    @Column(name = "specialization")
    private String specialization;

    // patient
    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

}
