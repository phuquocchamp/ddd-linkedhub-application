package com.phuquocchamp.profileservice.application.api;

import com.phuquocchamp.profileservice.application.dto.ServiceInfoDto;
import com.phuquocchamp.profileservice.application.dto.request.ProfileCreateRequest;
import com.phuquocchamp.profileservice.application.dto.request.ProfileUpdateRequest;
import com.phuquocchamp.profileservice.application.dto.response.ProfileFullResponse;
import com.phuquocchamp.profileservice.application.dto.response.ProfileResponse;
import com.phuquocchamp.profileservice.application.dto.response.ProfileUpdateResponse;
import com.phuquocchamp.profileservice.domain.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RefreshScope
@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {
    private final ProfileService profileService;
    private final ServiceInfoDto serviceInfo;

    public ProfileController(ProfileService profileService, ServiceInfoDto serviceInfo) {
        this.profileService = profileService;
        this.serviceInfo = serviceInfo;
    }

    @GetMapping("/info")
    public ResponseEntity<ServiceInfoDto> getInfo() {
        return new ResponseEntity<>(serviceInfo, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ProfileResponse> createProfile(@RequestHeader("X-User-ID") String userID, @Valid @RequestBody ProfileCreateRequest request) {
        return new ResponseEntity<>(profileService.createProfile(userID, request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@RequestHeader("X-User-ID") String userID) {
        return new ResponseEntity<>(profileService.getProfile(userID), HttpStatus.OK);
    }

    @GetMapping("/full")
    public ResponseEntity<ProfileFullResponse> getFullProfile(@RequestHeader("X-User-ID") String userID) {
        return new ResponseEntity<>(profileService.getFullProfile(userID), HttpStatus.OK);
    }

    @PutMapping("/{profileID}")
    public ResponseEntity<ProfileUpdateResponse> updateProfile(@RequestHeader("X-User-ID") String userID, @PathVariable("profileID") String profileID, @RequestBody ProfileUpdateRequest request) {
        return new ResponseEntity<>(profileService.updateProfile(userID, profileID, request), HttpStatus.OK);
    }

    @DeleteMapping("/{profileID}")
    public ResponseEntity<Void> deleteProfile(@RequestHeader("X-User-ID") String userID, @PathVariable("profileID") String profileID) {
        profileService.deleteProfile(userID, profileID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


