package com.example.Smart_Doc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor

public class UserTokenDto {

    private String token;
    private String email;
    private UserDto user;
}
