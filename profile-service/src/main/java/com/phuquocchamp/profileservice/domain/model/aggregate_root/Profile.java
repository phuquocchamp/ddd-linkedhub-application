package com.phuquocchamp.profileservice.domain.model.aggregate_root;

import com.phuquocchamp.profileservice.domain.model.entity.Certificate;
import com.phuquocchamp.profileservice.domain.model.entity.Education;
import com.phuquocchamp.profileservice.domain.model.entity.Experience;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profile")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private UUID profileID;
    @Column(nullable = false)
    private UUID userID;
    private String fistName;
    private String lastName;

    private String headline;
    private String summary;
    private String location;
    private String profilePictureURL;
    private String phone;

    private boolean isPublic = true;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificate> certificates = new ArrayList<>();
}
