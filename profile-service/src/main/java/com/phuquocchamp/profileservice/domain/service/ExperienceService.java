package com.phuquocchamp.profileservice.domain.service;

import com.phuquocchamp.profileservice.application.dto.request.ExperienceRequest;
import com.phuquocchamp.profileservice.application.dto.response.ExperienceResponse;
import com.phuquocchamp.profileservice.domain.model.aggregate_root.Profile;
import com.phuquocchamp.profileservice.domain.model.entity.Experience;
import com.phuquocchamp.profileservice.domain.repository.ExperienceRepository;
import com.phuquocchamp.profileservice.domain.repository.ProfileRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ExperienceService {
    private final ProfileRepository profileRepository;
    private final ExperienceRepository experienceRepository;
    private final ModelMapper modelMapper;

    public ExperienceService(ProfileRepository profileRepository, ExperienceRepository experienceRepository, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.experienceRepository = experienceRepository;
        this.modelMapper = modelMapper;

        modelMapper.typeMap(ExperienceRequest.class, Experience.class)
                .addMappings(mapper -> {
                    mapper.map(ExperienceRequest::getStartDate, (dest, value) -> dest.getDateRange().setStartDate((LocalDate) value));
                    mapper.map(ExperienceRequest::getEndDate, (dest, value) -> dest.getDateRange().setEndDate((LocalDate) value));
                });

        modelMapper.typeMap(Experience.class, ExperienceResponse.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getDateRange().getStartDate(), ExperienceResponse::setStartDate);
                    mapper.map(src -> src.getDateRange().getEndDate(), ExperienceResponse::setEndDate);
                });
    }


    public ExperienceResponse createExperience(String userID, String profileID, ExperienceRequest request) {
        UUID profileUuid;
        try {
            profileUuid = UUID.fromString(profileID);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid profile ID format");
        }

        Profile retrievedProfile = profileRepository.findByProfileID(profileUuid).orElseThrow(
                () -> new IllegalArgumentException("Profile not found")
        );

        if (!retrievedProfile.getUserID().toString().equals(userID)) {
            throw new SecurityException("User does not have permission to modify this profile");
        }

        Experience experience;
        try {
            experience = modelMapper.map(request, Experience.class);
        } catch (Exception e) {
            throw new RuntimeException("Error mapping ExperienceRequest to Experience", e);
        }
        experience.setExperienceID(UUID.randomUUID());
        experience.setProfile(retrievedProfile);

        experienceRepository.save(experience);
        return modelMapper.map(experience, ExperienceResponse.class);
    }


   public ExperienceResponse updateExperience(String profileID, String experienceID, @Valid ExperienceRequest request) {
    UUID profileUuid;
    UUID experienceUuid;
    try {
        profileUuid = UUID.fromString(profileID);
        experienceUuid = UUID.fromString(experienceID);
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid profile ID or experience ID format");
    }

    Profile retrievedProfile = profileRepository.findByProfileID(profileUuid).orElseThrow(
            () -> new IllegalArgumentException("Profile not found")
    );


    Experience existingExperience = experienceRepository.findByExperienceID(experienceUuid).orElseThrow(
            () -> new IllegalArgumentException("Experience not found")
    );

    if (!existingExperience.getProfile().getProfileID().equals(profileUuid)) {
        throw new SecurityException("Experience does not belong to the specified profile");
    }

    try {
        modelMapper.map(request, existingExperience);
    } catch (Exception e) {
        throw new RuntimeException("Error mapping ExperienceRequest to Experience", e);
    }

    experienceRepository.save(existingExperience);
    return modelMapper.map(existingExperience, ExperienceResponse.class);
}

}
