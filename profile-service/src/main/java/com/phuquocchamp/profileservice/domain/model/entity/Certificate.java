package com.phuquocchamp.profileservice.domain.model.entity;

import com.phuquocchamp.profileservice.domain.model.aggregate_root.Profile;
import com.phuquocchamp.profileservice.domain.model.value_object.DateRange;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certificate")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    @Id
    private UUID certificateID;
    private String title;
    private String description;
    private LocalDateTime issuedDate;
    private String issuedBy;

    @Embedded
    private DateRange dateRange;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
