package com.phuquocchamp.userprofile.infrastructure.rest;

import com.phuquocchamp.userprofile.application.ProfileApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {
    private final ProfileApplicationService service;

    public ProfileController(ProfileApplicationService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProfile(@RequestBody CreateProfileRequest request){
        service.createProfile(request.userId(), request.name(), request.email());
        return new ResponseEntity<>("Created Profile Successfully", HttpStatus.CREATED);
    }
}


// DTOs for request
record CreateProfileRequest(String userId, String name, String email) {
}

record UpdateProfileRequest(String name, String email) {
}

record AddEducationRequest(String school, String degree, String fieldOfStudy, LocalDate startDate, LocalDate endDate) {
}