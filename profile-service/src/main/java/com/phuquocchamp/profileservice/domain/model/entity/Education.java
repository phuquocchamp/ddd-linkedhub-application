package com.phuquocchamp.profileservice.domain.model.entity;

import com.phuquocchamp.profileservice.domain.model.aggregate_root.Profile;
import com.phuquocchamp.profileservice.domain.model.value_object.DateRange;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "education")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Education {
    @Id
    private UUID educationID;

    private String school;
    private String degree;
    private String fieldOfStudy;
    private String description;

    @Embedded
    private DateRange dateRange;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
