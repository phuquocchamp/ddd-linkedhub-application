package com.phuquocchamp.authservice.domain.repository;

import com.phuquocchamp.authservice.domain.model.aggregate_root.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserID(UUID userID);
    User save(User user);
}
