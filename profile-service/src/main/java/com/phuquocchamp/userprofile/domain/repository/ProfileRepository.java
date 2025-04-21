package com.phuquocchamp.userprofile.domain.repository;

import com.phuquocchamp.userprofile.domain.model.aggregate_root.Profile;
import com.phuquocchamp.userprofile.domain.model.value_object.Email;

import java.util.Optional;

public interface ProfileRepository {
    Optional<Profile> findById(String id);
    Optional<Profile> findByEmail(Email email);
    Profile save(Profile profile);
    void delete(Profile profile);
}
