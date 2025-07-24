package com.slippery.leaveapplication.services.impl;

import com.slippery.leaveapplication.dto.LoginRequest;
import com.slippery.leaveapplication.dto.UserDto;
import com.slippery.leaveapplication.dto.UserRequest;
import com.slippery.leaveapplication.dto.UserResponse;
import com.slippery.leaveapplication.models.Users;
import com.slippery.leaveapplication.repository.UserRepository;
import com.slippery.leaveapplication.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder =new BCryptPasswordEncoder(12);
    private final ModelMapper modelMapper =new ModelMapper();
    private final AuthenticationManager authenticationManager;

    public UserServiceImplementation(UserRepository repository, AuthenticationManager authenticationManager) {
        this.repository = repository;

        this.authenticationManager = authenticationManager;
    }
    private String generateRandomString(){
        UUID random = UUID.randomUUID();
        return random.toString();
    }

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        UserResponse response =new UserResponse();
        try{
            Users user = Users.builder()
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .createdOn(LocalDateTime.now())
                    .department(userRequest.getDepartment())
                    .email(userRequest.getEmail())
                    .fullName(userRequest.getFullName())
                    .profilePhoto(null)
                    .username(userRequest.getFullName().concat(generateRandomString().substring(0,4)))
                    .role(userRequest.getRole())
                    .build();
            repository.save(user);

            UserDto userDto =modelMapper.map(user,UserDto.class);
            response.setMessage("new user "+user.getFullName()+" created successfully");
            response.setStatusCode(201);
            response.setUser(userDto);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public UserResponse loginUser(LoginRequest loginRequest) {
        UserResponse response =new UserResponse();
        Optional<Users> user =repository.findByEmail(loginRequest.getEmail());
        if(user.isEmpty()){
            response.setMessage("User does not exist");
            response.setStatusCode(200);
            return response;
        }
        String username =user.get().getUsername();
        Authentication authentication =authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username,loginRequest.getPassword()));

        if(authentication.isAuthenticated()){
            response.setMessage("User authenticated successfully");
            response.setStatusCode(200);
            response.setUser(modelMapper.map(user,UserDto.class));
            return response;
        }
        response.setMessage("User authentication failed because of a wrong password");
        response.setStatusCode(400);
        return response;
    }

    @Override
    public UserResponse getUserWithId(String userId) {
        UserResponse response =new UserResponse();
        var user =repository.findById(userId);
        if(user.isEmpty()){
            response.setMessage("User with id "+ userId+" does not exist");
            response.setStatusCode(404);
            return response;
        }
        response.setUser(modelMapper.map(user,UserDto.class));
        response.setMessage("User with id "+userId+" found");
        response.setStatusCode(200);
        return response;
    }

    @Override
    public UserResponse deleteUserWithId(String userId) {
        UserResponse response =new UserResponse();
        var existingUser =getUserWithId(userId);
        if(existingUser.getStatusCode() !=200){
            return existingUser;
        }
        repository.deleteById(userId);
        response.setMessage("User with id"+ userId+" deleted successfully");
        response.setStatusCode(204);
        return response;
    }

    @Override
    public UserResponse deleteProfilePhoto(String userId) {
        UserResponse response =new UserResponse();
        var existingUser =getUserWithId(userId);
        if(existingUser.getStatusCode() != 200){
            return existingUser;
        }
        existingUser.getUser().setProfilePhoto(null);
        Users user =modelMapper.map(existingUser.getUser(),Users.class);
        repository.save(user);
        UserDto userResponse =modelMapper.map(user, UserDto.class);
        response.setUser(userResponse);
        response.setMessage("Profile photo deleted successfully");
        response.setStatusCode(204);
        return response;

    }

    @Override
    public UserResponse uploadProfilePhoto(MultipartFile image,String userId) throws IOException {
        UserResponse response =new UserResponse();
        var existingUser =getUserWithId(userId);
        if(existingUser.getStatusCode() != 200){
            return existingUser;
        }
        Users user =modelMapper.map(existingUser.getUser(),Users.class);

        user.setProfilePhoto(image.getBytes());
        repository.save(user);
        UserDto userResponse =modelMapper.map(user, UserDto.class);
        response.setUser(userResponse);
        response.setMessage("Profile photo updated successfully");
        response.setStatusCode(200);
        return response;
    }
}
