package com.phuquocchamp.profileservice.domain.repository;

import com.phuquocchamp.profileservice.domain.model.entity.Experience;

import java.util.Optional;
import java.util.UUID;

public interface ExperienceRepository {
    Experience save(Experience experience);
    Optional<Experience> findByExperienceID(UUID experienceID);
}
