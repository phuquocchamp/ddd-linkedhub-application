package com.phuquocchamp.profileservice.domain.model.value_object;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DateRange {
    private LocalDate startDate;
    private LocalDate endDate;
}
