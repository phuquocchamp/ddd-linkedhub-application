package com.phuquocchamp.profileservice.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;

    private String headline;
    private String summary;
    private String location;
    private String profilePictureURL;
    private String phone;
    private boolean isPublic;
}

