package com.phuquocchamp.profileservice.infrastructure.repository;

import com.phuquocchamp.profileservice.domain.model.aggregate_root.Profile;
import com.phuquocchamp.profileservice.domain.repository.ProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProfileRepository extends ProfileRepository, JpaRepository<Profile, Long> {
}
