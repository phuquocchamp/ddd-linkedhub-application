package com.phuquocchamp.profileservice.infrastructure.repository;

import com.phuquocchamp.profileservice.domain.model.aggregate_root.Profile;
import com.phuquocchamp.profileservice.domain.repository.ProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaProfileRepository extends ProfileRepository, JpaRepository<Profile, UUID> {
    @Query("SELECT p FROM Profile p " +
           "LEFT JOIN FETCH p.educations " +
           "LEFT JOIN FETCH p.experiences " +
           "LEFT JOIN FETCH p.certificates " +
           "WHERE p.userID = :userID")
    Optional<Profile> findFullByUserID(@Param("userID") UUID userID);
}