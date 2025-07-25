package com.slippery.leaveapplication.controller;

import com.slippery.leaveapplication.dto.LeaveApplicationDto;
import com.slippery.leaveapplication.models.LeaveApplications;
import com.slippery.leaveapplication.services.LeaveApplicationsService;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/leave")
public class LeaveApplicationsController  {
    private final LeaveApplicationsService service;

    public LeaveApplicationsController(LeaveApplicationsService service) {
        this.service = service;
    }

    @PostMapping("/{userId}/apply")
    public ResponseEntity<LeaveApplicationDto> createNewLeaveApplication(
            @PathVariable String userId,
            @RequestBody LeaveApplications applications) {
        var createdApplication =service.createNewLeaveApplication(userId, applications);

        return ResponseEntity.status(HttpStatusCode.valueOf(createdApplication.getStatusCode())).body(createdApplication);
    }
    @GetMapping("/get-by/{userId}")
    public ResponseEntity<LeaveApplicationDto> getApplicationsByUser(@PathVariable String userId){
        var userApplications =service.getApplicationsByUser(userId);
        return ResponseEntity.status(HttpStatusCode.valueOf(userApplications.getStatusCode())).body(userApplications);
    }
    @GetMapping("/get-in-review/{headId}")
    public ResponseEntity<LeaveApplicationDto> getApplicationsToReview(@PathVariable String headId){
        var applicationsToReview =service.getApplicationsToReview(headId);
        return ResponseEntity.status(HttpStatusCode.valueOf(applicationsToReview.getStatusCode())).body(applicationsToReview);
    }
    @PatchMapping("/{adminId}/approve/{applicationId}")
    public ResponseEntity<LeaveApplicationDto> approveApplication(@PathVariable String applicationId,@PathVariable String adminId){
        var approvedApplication =service.approveApplication(applicationId, adminId);
        return ResponseEntity.status(HttpStatusCode.valueOf(approvedApplication.getStatusCode())).body(approvedApplication);
    }
}
