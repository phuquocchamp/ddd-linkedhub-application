package com.phuquocchamp.userprofile.domain.model.aggregate_root;

import com.phuquocchamp.userprofile.domain.model.entity.Education;
import com.phuquocchamp.userprofile.domain.model.entity.Experience;
import com.phuquocchamp.userprofile.domain.model.value_object.Email;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private String id;

    private String name;

    @Embedded
    private Email email;

    private String headline;
    private String profilePictureUrl;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Education> educations = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Experience> experiences = new ArrayList<>();

    public Profile(String id, String name, Email email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public void addEducation(Education education) {
        educations.add(education);
    }

    public void addExperience(Experience experience) {
        experiences.add(experience);
    }

}
