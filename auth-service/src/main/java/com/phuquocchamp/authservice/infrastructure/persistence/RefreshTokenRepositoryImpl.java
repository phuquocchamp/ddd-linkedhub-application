package com.phuquocchamp.authservice.infrastructure.persistence;

import com.phuquocchamp.authservice.domain.model.entity.RefreshToken;
import com.phuquocchamp.authservice.domain.repository.RefreshTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRepositoryImpl extends RefreshTokenRepository, JpaRepository<RefreshToken, UUID> {
}
