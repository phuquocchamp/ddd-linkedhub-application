package com.phuquocchamp.profileservice.infrastructure.rest;

import com.phuquocchamp.profileservice.application.ProfileApplicationService;
import com.phuquocchamp.profileservice.application.dto.CreateProfileRequest;
import com.phuquocchamp.profileservice.application.dto.ServiceInfoDto;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RefreshScope
@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {
    private final ProfileApplicationService service;
    private final ServiceInfoDto serviceInfo;

    public ProfileController(ProfileApplicationService service, ServiceInfoDto serviceInfo) {
        this.service = service;
        this.serviceInfo = serviceInfo;
    }

    @GetMapping("/info")
    public ResponseEntity<ServiceInfoDto> getInfo() {
        return new ResponseEntity<>(serviceInfo, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProfile(@RequestBody CreateProfileRequest request){
        service.createProfile(request.userId(), request.name(), request.email());
        return new ResponseEntity<>("Created Profile Successfully", HttpStatus.CREATED);
    }
}


