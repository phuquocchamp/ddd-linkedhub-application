package com.phuquocchamp.profileservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCreateRequest {
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;

    private String headline;
    private String summary;
    private String location;
    private String profilePictureURL;
    private String phone;
    private boolean isPublic = true;
}
