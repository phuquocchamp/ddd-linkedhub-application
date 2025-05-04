package com.phuquocchamp.profileservice.infrastructure.repository;

import com.phuquocchamp.profileservice.domain.model.entity.Experience;
import com.phuquocchamp.profileservice.domain.repository.ExperienceRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaExperienceRepository extends ExperienceRepository, JpaRepository<Experience, UUID> {
}
