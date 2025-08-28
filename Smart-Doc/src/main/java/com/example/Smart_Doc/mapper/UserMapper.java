package com.example.Smart_Doc.mapper;

import com.example.Smart_Doc.model.dto.*;
import com.example.Smart_Doc.model.enums.UserRole;
import com.example.Smart_Doc.model.entity.*;

public class UserMapper {

    public static UserDto userEntityToUserDto(User user) {
        if (user == null) return null;
        
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());
        userDto.setProfilePictureUrl(user.getProfilePictureUrl());
        userDto.setAddress(user.getAddress());
        userDto.setGender(user.getGender());
        userDto.setNationality(user.getNationality());
        userDto.setDateOfBirth(user.getDateOfBirth());
        
        if (user instanceof Doctor) {
            Doctor doctor = (Doctor) user;
            userDto.setBio(doctor.getBio());
            userDto.setYearsOfExperience(doctor.getYearsOfExperience());
            userDto.setQualifications(doctor.getQualifications());
            userDto.setSpecialization(doctor.getSpecialization());
            userDto.setPracticeNumber(doctor.getPracticeNumber());
            userDto.setConsultationFee(doctor.getConsultationFee());
        } else if (user instanceof Patient) {
            Patient patient = (Patient) user;
            userDto.setEmergencyContactName(patient.getEmergencyContactName());
            userDto.setEmergencyContactPhone(patient.getEmergencyContactPhone());
            userDto.setMedicalHistory(patient.getMedicalHistory());
            userDto.setAllergies(patient.getAllergies());
        }
        
        return userDto;
    }

    public static User userRequestDtoToUserEntity(UserRequestDto userRequestDto) {
        if (userRequestDto == null) return null;
        
        User user;
        if (userRequestDto.getRole() == UserRole.DOCTOR) {
            Doctor doctor = new Doctor();
            doctor.setPracticeNumber(userRequestDto.getPracticeNumber());
            doctor.setSpecialization(userRequestDto.getSpecialization());
            doctor.setQualifications(userRequestDto.getQualifications());
            doctor.setYearsOfExperience(userRequestDto.getYearsOfExperience());
            doctor.setConsultationFee(userRequestDto.getConsultationFee());
            user = doctor;
        } else {
            Patient patient = new Patient();
            patient.setEmergencyContactName(userRequestDto.getEmergencyContactName());
            patient.setEmergencyContactPhone(userRequestDto.getEmergencyContactPhone());
            user = patient;
        }
        
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setRole(userRequestDto.getRole());
        user.setPassword(userRequestDto.getPassword());
        
        return user;
    }

    public static UserTokenDto userToUserTokenDTO(User user, String token) {
        if (user == null) return null;
        
        return UserTokenDto.builder()
                .user(userEntityToUserDto(user))
                .token(token)
                .email(user.getEmail())
                .build();
    }
}