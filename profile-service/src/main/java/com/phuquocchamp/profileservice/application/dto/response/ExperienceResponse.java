package com.phuquocchamp.profileservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceResponse {
    private UUID experienceID;

    private String company;
    private String position;
    private String role;
    private String description;
    private String location;

    private LocalDate startDate;
    private LocalDate endDate;
}
