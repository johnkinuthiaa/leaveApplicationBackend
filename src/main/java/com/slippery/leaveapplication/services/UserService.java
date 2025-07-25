package com.slippery.leaveapplication.services;

import com.slippery.leaveapplication.dto.UserRequest;
import com.slippery.leaveapplication.dto.UserResponse;
import com.slippery.leaveapplication.dto.LoginRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface UserService {
    UserResponse registerUser(UserRequest userRequest);
    UserResponse loginUser(LoginRequest loginRequest);
    UserResponse getUserWithId(String userId);
    UserResponse deleteUserWithId(String userId);
    UserResponse deleteProfilePhoto(String userId);
    UserResponse uploadProfilePhoto(MultipartFile image,String userId) throws IOException;
    UserResponse getAllUsers(String userId);
    UserResponse setLeaveDays(String employeeId,String adminId,Long leaveDays);
    UserResponse getHod(String departmentName);



}
