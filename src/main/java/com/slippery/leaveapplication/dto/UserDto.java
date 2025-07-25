package com.slippery.leaveapplication.dto;

import com.slippery.leaveapplication.models.Department;
import com.slippery.leaveapplication.models.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String id;
    private String fullName;
    private String email;
    private String username;
    private Department department;
    private Role role;
    private LocalDateTime createdOn;
    private Byte[] profilePhoto;
    private Long leaveDays;
    private Boolean isOnLeave;

}
