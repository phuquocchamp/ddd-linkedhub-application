package com.phuquocchamp.userprofile.domain.service;

import com.phuquocchamp.userprofile.domain.model.value_object.Email;
import com.phuquocchamp.userprofile.domain.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileValidationService {
    private final ProfileRepository repository;

    public ProfileValidationService(ProfileRepository repository) {
        this.repository = repository;
    }

    public void validateEmailUniqueness(Email email, String excludeUserId){
        repository.findByEmail(email)
                .ifPresent(userProfile -> {
                    if(!userProfile.getId().equals(excludeUserId)){
                        throw new IllegalArgumentException("Email already exists");
                    }
                });
    }
}
