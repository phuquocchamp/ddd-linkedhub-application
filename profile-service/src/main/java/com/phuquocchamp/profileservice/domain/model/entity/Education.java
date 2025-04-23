package com.phuquocchamp.profileservice.domain.model.entity;

import com.phuquocchamp.profileservice.domain.model.value_object.DateRange;
import jakarta.persistence.*;

@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String school;
    private String degree;
    private String fieldOfStudy;

    @Embedded
    private DateRange dateRange;

    protected Education() {}

    public Education(String school, String degree, String fieldOfStudy, DateRange dateRange) {
        this.school = school;
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
        this.dateRange = dateRange;
    }

}
