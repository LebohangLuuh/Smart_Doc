package com.example.Smart_Doc.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("DOCTOR")
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends User {

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "qualifications", columnDefinition = "TEXT")
    private String qualifications;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "practice_number", unique = true)
    private String practiceNumber;

    @Column(name = "consultation_fee")
    private Double consultationFee;
}