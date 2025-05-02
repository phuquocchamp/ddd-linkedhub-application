package com.phuquocchamp.profileservice.domain.model.entity;

import com.phuquocchamp.profileservice.domain.model.value_object.DateRange;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "experience")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    @Id
    private UUID experienceID;

    private String company;
    private String position;
    private String role;
    private String description;
    private String location;

    @Embedded
    private DateRange dateRange;

}
