package com.slippery.leaveapplication.repository;

import com.slippery.leaveapplication.models.LeaveApplications;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeaveApplicationRepository extends MongoRepository<LeaveApplications,String> {
}
