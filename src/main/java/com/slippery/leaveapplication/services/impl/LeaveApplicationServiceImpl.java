package com.slippery.leaveapplication.services.impl;

import com.slippery.leaveapplication.dto.LeaveApplicationDto;
import com.slippery.leaveapplication.dto.UserDto;
import com.slippery.leaveapplication.models.LeaveApplications;
import com.slippery.leaveapplication.models.Status;
import com.slippery.leaveapplication.models.Users;
import com.slippery.leaveapplication.repository.LeaveApplicationRepository;
import com.slippery.leaveapplication.repository.UserRepository;
import com.slippery.leaveapplication.services.LeaveApplicationsService;
import com.slippery.leaveapplication.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationsService {
    private final LeaveApplicationRepository repository;
    private final UserService userService;
    private final ModelMapper modelMapper =new ModelMapper();
    private final UserRepository userRepository;

    public LeaveApplicationServiceImpl(LeaveApplicationRepository repository,
                                       UserService userService,
                                       UserRepository userRepository) {
        this.repository = repository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public LeaveApplicationDto createNewLeaveApplication(String userId, LeaveApplications applications) {
        LeaveApplicationDto response =new LeaveApplicationDto();
        var existingUser =userService.getUserWithId(userId);
        if(existingUser.getStatusCode() !=200){
            return modelMapper.map(existingUser,LeaveApplicationDto.class);
        }
        var user =existingUser.getUser();
        var department =user.getDepartment().name();
        var role =user.getRole().name();
        if(role.equals("EMPLOYEE")){
//            call employee application
            return employeeApplication(department,user,userId,applications);
        }else if(role.equals("HOD")){

        }
        return response;
    }
    private LeaveApplicationDto employeeApplication(
            String department,
            UserDto user,
            String userId,
            LeaveApplications applications){

        LeaveApplicationDto response =new LeaveApplicationDto();
        var hod =userService.getHod(department);
        if(hod.getStatusCode() !=200){
            return modelMapper.map(hod,LeaveApplicationDto.class);
        }
        applications.setAppliedOn(new Date());
        applications.setStatus(Status.PENDING);
        applications.setUsername(user.getUsername());
        applications.setEmail(user.getEmail());
        applications.setUserId(user.getId());
        repository.save(applications);

        var userApplications =user.getApplicationsMade();
        userApplications.add(applications);
        user.setApplicationsMade(userApplications);
        userRepository.save(modelMapper.map(user, Users.class));

//            send to head
        var departmentHead =hod.getUser();
        var departmentHeadApplicationsToReview =departmentHead.getApplicationsToReview();
        departmentHeadApplicationsToReview.add(applications);

        departmentHead.setApplicationsToReview(departmentHeadApplicationsToReview);
        userRepository.save(modelMapper.map(departmentHead, Users.class));
        response.setMessage("New leave application created ");
        response.setStatusCode(201);
        response.setLeaveApplication(applications);
        return response;
    }
}
