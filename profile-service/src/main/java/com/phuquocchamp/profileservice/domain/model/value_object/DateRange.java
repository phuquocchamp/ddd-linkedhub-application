package com.phuquocchamp.profileservice.domain.model.value_object;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class DateRange {
    private LocalDate startDate;
    private LocalDate endDate;
    public DateRange() {}
    public DateRange(LocalDate startDate, LocalDate endDate) {
        if(endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
}
