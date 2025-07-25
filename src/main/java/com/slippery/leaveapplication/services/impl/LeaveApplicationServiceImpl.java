package com.slippery.leaveapplication.services.impl;

import com.slippery.leaveapplication.LeaveApplication;
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
            return employeeApplication(department,user,applications);
        }else if(role.equals("HOD")){
            return hodApplication(user,applications);
        }else {
            response.setMessage("Error creating an application for you foreign mister!!");
            response.setStatusCode(401);
        }
        return response;
    }

    @Override
    public LeaveApplicationDto getApplicationsByUser(String userId) {
        LeaveApplicationDto response =new LeaveApplicationDto();
        var existingUser =userService.getUserWithId(userId);
        if(existingUser.getStatusCode() !=200){
            return modelMapper.map(existingUser,LeaveApplicationDto.class);
        }
        var user =existingUser.getUser();
        var applications =user.getApplicationsMade();
        response.setLeaveApplications(applications);
        response.setStatusCode(200);
        response.setMessage("All applications by "+user.getFullName());
        return response;
    }

    @Override
    public LeaveApplicationDto getApplicationsToReview(String headId) {
        return null;
    }

    @Override
    public LeaveApplication approveApplication(String applicationId, String adminId) {
        return null;
    }

    private LeaveApplicationDto hodApplication(
            UserDto user,
            LeaveApplications application
    ){
        LeaveApplicationDto response =new LeaveApplicationDto();

        try{
            application.setUserId(user.getId());
            application.setUsername(user.getUsername());
            application.setEmail(user.getEmail());
            application.setAppliedOn(new Date());
            application.setStatus(Status.PENDING);
            repository.save(application);

//        update info for the HOD
            var hodApplications =user.getApplicationsMade();
            hodApplications.add(application);
            user.setApplicationsMade(hodApplications);
            var updatedHodInfo =modelMapper.map(user, Users.class);
            userRepository.save(updatedHodInfo);

//        get ceo
            var CEOResponse =userService.getCEO();
            if(CEOResponse.getStatusCode() !=200){
                return modelMapper.map(CEOResponse,LeaveApplicationDto.class);
            }
//        Update info for the CEO
            var CEO =CEOResponse.getUser();
            var ceoApplicationsToReview =CEO.getApplicationsToReview();
            ceoApplicationsToReview.add(application);
            CEO.setApplicationsToReview(ceoApplicationsToReview);

            var updatedCeoApplicationsToReview =modelMapper.map(CEO, Users.class);

            userRepository.save(updatedCeoApplicationsToReview);
            response.setStatusCode(201);
            response.setMessage("New application for "+user.getFullName() +" was created successfully and is waiting for review");
            response.setLeaveApplication(application);
            return response;
        } catch (Exception e) {
            response.setStatusCode(201);
            response.setMessage("Error creating a New application for "+user.getFullName() +" because "+e.getLocalizedMessage());
            response.setLeaveApplication(application);
            return response;
        }

    }
    private LeaveApplicationDto employeeApplication(
            String department,
            UserDto user,
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
