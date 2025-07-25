package com.slippery.leaveapplication.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LeaveApplications {
    @Id
    private String id;
    private String username;
    private String email;
    private LeaveType leaveType;
    private String userId;
    private Long days;
    private Date startDate;
    private Date endDate;
    private Status status;
    private Date appliedOn =new Date();
    private String contact;


}
