package com.phuquocchamp.authservice.infrastructure.persistence;

import com.phuquocchamp.authservice.domain.model.aggregate_root.User;
import com.phuquocchamp.authservice.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryImpl extends UserRepository, JpaRepository<User, Long> {

}
