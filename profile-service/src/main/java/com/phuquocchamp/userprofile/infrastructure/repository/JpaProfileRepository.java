package com.phuquocchamp.userprofile.infrastructure.repository;

import com.phuquocchamp.userprofile.domain.model.aggregate_root.Profile;
import com.phuquocchamp.userprofile.domain.repository.ProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProfileRepository extends ProfileRepository, JpaRepository<Profile, Long> {
}
