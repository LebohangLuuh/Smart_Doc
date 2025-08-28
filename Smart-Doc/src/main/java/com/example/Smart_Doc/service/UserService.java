package com.example.Smart_Doc.service;

import com.example.Smart_Doc.model.dto.UserDto;
import com.example.Smart_Doc.model.dto.UserRequestDto;
import com.example.Smart_Doc.model.entity.User;

import com.example.Smart_Doc.model.dto.*;


public interface UserService {

        public User getUserByEmail(String email);

        public UserDto registerUser(UserRequestDto userRequestDto);

        public UserDto loginUser(LoginRequestDto loginRequest);

        UserDto updateUserDetails(UpdateUserRequestDto updateUserRequestDto);

        void resetPassword(String newPassword, String token);

        public void forgotPassword(String email);

}
