package com.slippery.leaveapplication.controller;

import com.slippery.leaveapplication.dto.LeaveApplicationDto;
import com.slippery.leaveapplication.models.LeaveApplications;
import com.slippery.leaveapplication.services.LeaveApplicationsService;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
