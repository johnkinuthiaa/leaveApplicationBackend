package com.slippery.leaveapplication.services.impl;

import com.slippery.leaveapplication.dto.LeaveApplicationDto;
import com.slippery.leaveapplication.models.LeaveApplications;
import com.slippery.leaveapplication.repository.LeaveApplicationRepository;
import com.slippery.leaveapplication.services.LeaveApplicationsService;
import com.slippery.leaveapplication.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationsService {
    private final LeaveApplicationRepository repository;
    private final UserService userService;
    private final ModelMapper modelMapper =new ModelMapper();

    public LeaveApplicationServiceImpl(LeaveApplicationRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public LeaveApplicationDto createNewLeaveApplication(String userId, LeaveApplications applications) {
        LeaveApplicationDto response =new LeaveApplicationDto();
        var existingUser =userService.getUserWithId(userId);
        if(existingUser.getStatusCode() !=200){
            return modelMapper.map(existingUser,LeaveApplicationDto.class);
        }
        var user =existingUser.getUser();
        var department =user.getDepartment();
        var role =user.getRole();
        if(role.name().equals("EMPLOYEE")){

        }
        return null;
    }
}
