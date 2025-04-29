package com.phuquocchamp.authservice.domain.repository;

import com.phuquocchamp.authservice.domain.model.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken save(RefreshToken refreshToken);
}
