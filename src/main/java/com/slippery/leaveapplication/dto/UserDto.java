package com.slippery.leaveapplication.dto;

import com.slippery.leaveapplication.models.Department;
import com.slippery.leaveapplication.models.LeaveApplications;
import com.slippery.leaveapplication.models.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<LeaveApplications> applicationsMade =new ArrayList<>();
    private List<LeaveApplications> applicationsToReview =new ArrayList<>();

}
