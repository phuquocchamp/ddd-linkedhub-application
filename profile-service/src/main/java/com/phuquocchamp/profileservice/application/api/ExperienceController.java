package com.phuquocchamp.profileservice.application.api;

import com.phuquocchamp.profileservice.application.dto.request.ExperienceRequest;
import com.phuquocchamp.profileservice.application.dto.response.ExperienceResponse;
import com.phuquocchamp.profileservice.domain.service.ExperienceService;
import com.phuquocchamp.profileservice.domain.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles/{profileID}/experiences")
public class ExperienceController {
    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @PutMapping
    public ResponseEntity<ExperienceResponse> createExperience(
            @RequestHeader("X-User-ID") String userID,
            @PathVariable("profileID") String profileID,
            @Valid @RequestBody ExperienceRequest request
    ) {
        return new ResponseEntity<>(experienceService.createExperience(userID, profileID, request), HttpStatus.CREATED);
    }

    @PutMapping("{experienceID}")
    public ResponseEntity<ExperienceResponse> updateExperience(
            @PathVariable("profileID") String profileID,
            @PathVariable("experienceID") String experienceID,
            @Valid @RequestBody ExperienceRequest request
       ){
        return new ResponseEntity<>(experienceService.updateExperience(profileID, experienceID, request), HttpStatus.OK);
    }

}
