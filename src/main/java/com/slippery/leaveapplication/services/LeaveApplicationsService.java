package com.slippery.leaveapplication.services;


import com.slippery.leaveapplication.dto.LeaveApplicationDto;
import com.slippery.leaveapplication.models.LeaveApplications;

public interface LeaveApplicationsService {
    LeaveApplicationDto createNewLeaveApplication(String userId, LeaveApplications applications);
}
