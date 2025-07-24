package com.slippery.leaveapplication.controller;

import com.slippery.leaveapplication.dto.LoginRequest;
import com.slippery.leaveapplication.dto.UserRequest;
import com.slippery.leaveapplication.dto.UserResponse;
import com.slippery.leaveapplication.services.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController  {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        var registeredUser =userService.registerUser(userRequest);

        return ResponseEntity.status(HttpStatusCode.valueOf(registeredUser.getStatusCode())).body(registeredUser);
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody LoginRequest loginRequest){
        var loggedInUser =userService.loginUser(loginRequest);
        return ResponseEntity.status(HttpStatusCode.valueOf(loggedInUser.getStatusCode())).body(loggedInUser);
    }
}
