package com.phuquocchamp.profileservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileFullResponse {
    private UUID profileID;
    private String firstName;
    private String lastName;
    private String headline;
    private String summary;
    private String location;
    private String profilePictureURL;
    private String phone;
    private Boolean isPublic;
    private List<ExperienceResponse> experiences;
    private List<EducationResponse> educations;
    private List<CertificateResponse> certificates;
}
