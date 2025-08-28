package com.example.Smart_Doc.validation;

import com.example.Smart_Doc.model.dto.UserRequestDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRequestDto> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(UserRequestDto userRequestDto, ConstraintValidatorContext context) {
        if (userRequestDto == null) {
            return true; // Let @NotNull handle null validation
        }

        String password = userRequestDto.getPassword();
        String confirmPassword = userRequestDto.getConfirmPassword();

        // Check if passwords match
        boolean passwordsMatch = password != null && password.equals(confirmPassword);

        if (!passwordsMatch) {
            // Customize error message location
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return passwordsMatch;
    }
}