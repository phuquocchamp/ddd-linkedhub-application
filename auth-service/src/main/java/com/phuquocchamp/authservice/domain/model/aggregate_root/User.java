package com.phuquocchamp.authservice.domain.model.aggregate_root;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private UUID userID;

    @Column(unique = true)
    private String email;
    private String password;
    private Boolean isActive = false;

    private Boolean emailVerified = false;
    private String emailVerificationToken;
    private LocalDateTime emailVerificationExpiryDate = null;

    private String passwordResetToken;
    private LocalDateTime passwordResetExpiryDate = null;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    public User(String email, String password) {
        this.userID = UUID.randomUUID();
        this.email = email;
        this.password = password;
    }

}
