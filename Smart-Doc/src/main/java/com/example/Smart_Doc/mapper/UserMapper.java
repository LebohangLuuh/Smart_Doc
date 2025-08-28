package com.example.Smart_Doc.mapper;

import com.example.Smart_Doc.model.dto.UserDto;
import com.example.Smart_Doc.model.dto.UserRequestDto;
import com.example.Smart_Doc.model.entity.Doctor;
import com.example.Smart_Doc.model.entity.Patient;
import com.example.Smart_Doc.model.entity.User;
import com.example.Smart_Doc.model.enums.UserRole;

public class UserMapper {

    public static User userRequestDtoToUserEntity(UserRequestDto userRequestDto) {
        UserRole role = userRequestDto.getUserRole();
        
        User user;
        if (role == UserRole.DOCTOR) {
            user = new Doctor();
            if (userRequestDto.getPracticeNumber() != null && !userRequestDto.getPracticeNumber().trim().isEmpty()) {
                ((Doctor) user).setPracticeNumber(userRequestDto.getPracticeNumber());
            }
        } else {
            user = new Patient();
        }

        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setRole(role);
        
        // Note: Password will be encoded in the service layer
        user.setPassword(userRequestDto.getPassword());

        return user;
    }

    public static UserDto userEntityToUserDto(User user) {
        UserDto.UserDtoBuilder builder = UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .profilePictureUrl(user.getProfilePictureUrl())
                .gender(user.getGender())
                .nationality(user.getNationality())
                .dateOfBirth(user.getDateOfBirth())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt());

        // Add role-specific fields
        if (user instanceof Doctor) {
            Doctor doctor = (Doctor) user;
            builder.practiceNumber(doctor.getPracticeNumber())
                   .bio(doctor.getBio())
                   .yearsOfExperience(doctor.getYearsOfExperience())
                   .qualifications(doctor.getQualifications())
                   .specialization(doctor.getSpecialization())
                   .consultationFee(doctor.getConsultationFee());
        } else if (user instanceof Patient) {
            Patient patient = (Patient) user;
            builder.emergencyContactName(patient.getEmergencyContactName())
                   .emergencyContactPhone(patient.getEmergencyContactPhone())
                   .medicalHistory(patient.getMedicalHistory())
                   .allergies(patient.getAllergies());
        }

        return builder.build();
    }
}