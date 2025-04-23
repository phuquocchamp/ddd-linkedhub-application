package com.phuquocchamp.profileservice.domain.model.entity;

import com.phuquocchamp.profileservice.domain.model.value_object.DateRange;
import jakarta.persistence.*;

@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String position;
    private String description;

    @Embedded
    private DateRange dateRange;

    protected Experience() {}

    public Experience(String company, String position, String description, DateRange dateRange) {
        this.company = company;
        this.position = position;
        this.description = description;
        this.dateRange = dateRange;
    }

}
