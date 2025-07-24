package com.slippery.leaveapplication.dto;

import com.slippery.leaveapplication.models.Department;
import com.slippery.leaveapplication.models.Role;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRequest {
    private String fullName;
    private String email;
    private String password;
    private Department department;
    private Role role;
    private LocalDateTime createdOn =LocalDateTime.now();

}
