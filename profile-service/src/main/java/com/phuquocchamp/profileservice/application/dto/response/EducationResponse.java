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
public class EducationResponse {
    private UUID educationID;
    private String school;
    private String degree;
    private String fieldOfStudy;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;
}
