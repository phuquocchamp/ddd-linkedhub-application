package com.phuquocchamp.profileservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private UUID profileID;
    private String firstName;
    private String lastName;

    private String headline;
    private String summary;
    private String location;
    private String profilePictureURL;
    private String phone;
    private boolean isPublic;
}
