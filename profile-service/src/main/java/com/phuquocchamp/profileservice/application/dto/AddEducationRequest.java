package com.phuquocchamp.profileservice.application.dto;

import java.time.LocalDate;

public record AddEducationRequest(String school, String degree, String fieldOfStudy, LocalDate startDate, LocalDate endDate) {
}