package com.slippery.leaveapplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.slippery.leaveapplication.models.LeaveApplications;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaveApplicationDto {
    private String message;
    private int statusCode;
    private LeaveApplications leaveApplication;
    private List<LeaveApplications> leaveApplications ;
}
