package com.slippery.leaveapplication.controller;

import com.slippery.leaveapplication.dto.LoginRequest;
import com.slippery.leaveapplication.dto.UserRequest;
import com.slippery.leaveapplication.dto.UserResponse;
import com.slippery.leaveapplication.services.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @GetMapping("/get/{userId}")
    public ResponseEntity<UserResponse> getUserWithId(@PathVariable String userId){
        var user =userService.getUserWithId(userId);
        return ResponseEntity.status(HttpStatusCode.valueOf(user.getStatusCode())).body(user);
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserResponse> deleteUserWithId(@PathVariable String userId){
        var deletedUser =userService.deleteUserWithId(userId);
        return ResponseEntity.status(HttpStatusCode.valueOf(deletedUser.getStatusCode())).body(deletedUser);
    }
    @PutMapping("/{userId}/delete-profile")
    public ResponseEntity<UserResponse> deleteProfilePhoto(@PathVariable String userId){
        var deleteProfilePhoto =userService.deleteProfilePhoto(userId);
        return ResponseEntity.status(HttpStatusCode.valueOf(deleteProfilePhoto.getStatusCode())).body(deleteProfilePhoto);
    }
    @PatchMapping("/{userId}/upload-profile")
    public ResponseEntity<UserResponse> uploadProfilePhoto(@RequestPart MultipartFile image, @PathVariable String userId) throws IOException{
        var uploadedImage =userService.uploadProfilePhoto(image,userId);
        return ResponseEntity.status(HttpStatusCode.valueOf(uploadedImage.getStatusCode())).body(uploadedImage);
    }
}
