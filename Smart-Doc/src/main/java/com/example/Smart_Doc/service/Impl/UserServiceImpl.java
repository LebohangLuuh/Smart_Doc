package com.example.Smart_Doc.service.Impl;

import com.example.Smart_Doc.exception.AuthenticationException;
import com.example.Smart_Doc.exception.DataNotFoundException;
import com.example.Smart_Doc.mapper.UserMapper;
import com.example.Smart_Doc.model.dto.LoginRequestDto;
import com.example.Smart_Doc.model.dto.UpdateUserRequestDto;
import com.example.Smart_Doc.model.dto.UserDto;
import com.example.Smart_Doc.model.dto.UserRequestDto;
import com.example.Smart_Doc.model.entity.Doctor;
import com.example.Smart_Doc.model.entity.Patient;
import com.example.Smart_Doc.model.entity.User;
import com.example.Smart_Doc.model.enums.UserRole;
import com.example.Smart_Doc.repository.UserRepository;
import com.example.Smart_Doc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // private final EmailService emailService; // Inject when available

    @Value("${app.frontend.reset-password-url:http://localhost:3000/reset-password}")
    private String resetPasswordFrontendUrl;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional
    public UserDto registerUser(UserRequestDto userRequestDto) {
        // Check if user already exists
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Create user entity
        User user = UserMapper.userRequestDtoToUserEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        
        // Save user
        User savedUser = userRepository.save(user);
        
        return UserMapper.userEntityToUserDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto loginUser(LoginRequestDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        return UserMapper.userEntityToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUserDetails(UpdateUserRequestDto updateUserRequestDto) {
        User user = userRepository.findByEmail(updateUserRequestDto.getEmail())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        // Update common fields
        if (updateUserRequestDto.getFirstName() != null && !updateUserRequestDto.getFirstName().trim().isEmpty()) {
            user.setFirstName(updateUserRequestDto.getFirstName());
        }
        if (updateUserRequestDto.getLastName() != null && !updateUserRequestDto.getLastName().trim().isEmpty()) {
            user.setLastName(updateUserRequestDto.getLastName());
        }
        if (updateUserRequestDto.getPhoneNumber() != null && !updateUserRequestDto.getPhoneNumber().trim().isEmpty()) {
            user.setPhoneNumber(updateUserRequestDto.getPhoneNumber());
        }
        if (updateUserRequestDto.getAddress() != null) {
            user.setAddress(updateUserRequestDto.getAddress());
        }
        if (updateUserRequestDto.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(updateUserRequestDto.getProfilePictureUrl());
        }
        if (updateUserRequestDto.getGender() != null) {
            user.setGender(updateUserRequestDto.getGender());
        }
        if (updateUserRequestDto.getNationality() != null) {
            user.setNationality(updateUserRequestDto.getNationality());
        }
        if (updateUserRequestDto.getDateOfBirth() != null) {
            user.setDateOfBirth(updateUserRequestDto.getDateOfBirth());
        }

        // Update role-specific fields
        if (user instanceof Doctor && user.getRole() == UserRole.DOCTOR) {
            Doctor doctor = (Doctor) user;
            if (updateUserRequestDto.getBio() != null) {
                doctor.setBio(updateUserRequestDto.getBio());
            }
            if (updateUserRequestDto.getYearsOfExperience() != null) {
                doctor.setYearsOfExperience(updateUserRequestDto.getYearsOfExperience());
            }
            if (updateUserRequestDto.getQualifications() != null) {
                doctor.setQualifications(updateUserRequestDto.getQualifications());
            }
            if (updateUserRequestDto.getSpecialization() != null) {
                doctor.setSpecialization(updateUserRequestDto.getSpecialization());
            }
            if (updateUserRequestDto.getConsultationFee() != null) {
                doctor.setConsultationFee(updateUserRequestDto.getConsultationFee());
            }
        } else if (user instanceof Patient && user.getRole() == UserRole.PATIENT) {
            Patient patient = (Patient) user;
            if (updateUserRequestDto.getEmergencyContactName() != null) {
                patient.setEmergencyContactName(updateUserRequestDto.getEmergencyContactName());
            }
            if (updateUserRequestDto.getEmergencyContactPhone() != null) {
                patient.setEmergencyContactPhone(updateUserRequestDto.getEmergencyContactPhone());
            }
            if (updateUserRequestDto.getMedicalHistory() != null) {
                patient.setMedicalHistory(updateUserRequestDto.getMedicalHistory());
            }
            if (updateUserRequestDto.getAllergies() != null) {
                patient.setAllergies(updateUserRequestDto.getAllergies());
            }
        }

        User updatedUser = userRepository.save(user);
        return UserMapper.userEntityToUserDto(updatedUser);
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24);

        user.setResetToken(resetToken);
        user.setResetTokenExpiryDate(expiryDate);
        userRepository.save(user);

        // TODO: Send email when EmailService is available
        String resetLink = resetPasswordFrontendUrl + "?token=" + resetToken;
        System.out.println("Reset link for " + email + ": " + resetLink);
    }

    @Override
    @Transactional
    public void resetPassword(String newPassword, String token) {
        User user = userRepository.findByResetTokenAndNotExpired(token, LocalDateTime.now())
                .orElseThrow(() -> new DataNotFoundException("Invalid or expired token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiryDate(null);
        userRepository.save(user);
    }
}