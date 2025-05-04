package com.phuquocchamp.profileservice.domain.repository;

import com.phuquocchamp.profileservice.domain.model.aggregate_root.Profile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository {
    Optional<Profile> findByProfileID(UUID profileID);
    Optional<Profile> findByUserID(UUID userID);
    Optional<Profile> findFullByUserID(UUID userID);

    Profile save(Profile profile);

    void delete(Profile profile);
}
