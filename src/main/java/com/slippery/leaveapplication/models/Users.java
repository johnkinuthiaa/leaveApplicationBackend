package com.slippery.leaveapplication.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Users {
    @Id
    private String id;
    private String fullName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String username;
    private Department department;
    private Role role;
    private LocalDateTime createdOn;
    private Byte[] profilePhoto;

}
