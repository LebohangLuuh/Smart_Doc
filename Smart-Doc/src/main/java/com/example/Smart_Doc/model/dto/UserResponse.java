package com.example.Smart_Doc.model.dto;

import com.example.Smart_Doc.model.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {

    private Integer id;
    private String fullname;
    private String email;
    private String role;
    private String practiceNumber;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.fullname = user.getFullname();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.practiceNumber = user.getPracticeNumber();
        this.createdAt = user.getCreatedAt();
    }

}