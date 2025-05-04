package com.phuquocchamp.profileservice.domain.model.aggregate_root;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.phuquocchamp.profileservice.domain.model.entity.Certificate;
import com.phuquocchamp.profileservice.domain.model.entity.Education;
import com.phuquocchamp.profileservice.domain.model.entity.Experience;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "profile")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private UUID profileID;
    @Column(nullable = false, unique = true)
    private UUID userID;
    private String firstName;
    private String lastName;

    private String headline;
    private String summary;
    private String location;
    private String profilePictureURL;
    private String phone;

    private boolean isPublic = true;

    @JsonIgnore
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Education> educations = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Experience> experiences = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Certificate> certificates = new HashSet<>();
}
