package com.phuquocchamp.profileservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponse {
    private UUID certificateID;
    private String title;
    private String description;
    private LocalDateTime issuedDate;
    private String issuedBy;

    private LocalDate startDate;
    private LocalDate endDate;
}
