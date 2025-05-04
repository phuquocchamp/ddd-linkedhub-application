package com.phuquocchamp.profileservice.domain.service;

import com.phuquocchamp.profileservice.application.dto.request.ProfileCreateRequest;
import com.phuquocchamp.profileservice.application.dto.request.ProfileUpdateRequest;
import com.phuquocchamp.profileservice.application.dto.response.ProfileFullResponse;
import com.phuquocchamp.profileservice.application.dto.response.ProfileResponse;
import com.phuquocchamp.profileservice.application.dto.response.ProfileUpdateResponse;
import com.phuquocchamp.profileservice.domain.model.aggregate_root.Profile;
import com.phuquocchamp.profileservice.domain.repository.ProfileRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;

    public ProfileService(ProfileRepository profileRepository, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
    }

    public ProfileResponse createProfile(String userID, ProfileCreateRequest request) {
        Profile profile = modelMapper.map(request, Profile.class);
        profile.setUserID(UUID.fromString(userID));
        profile.setProfileID(UUID.randomUUID());

        Profile savedProfile = profileRepository.save(profile);

        return modelMapper.map(savedProfile, ProfileResponse.class);
    }

    public ProfileFullResponse getFullProfile(String userID) {
        Profile profile = profileRepository.findFullByUserID(UUID.fromString(userID)).orElseThrow(
                () -> new IllegalArgumentException("Profile not found")
        );
        return modelMapper.map(profile, ProfileFullResponse.class);
    }

    public ProfileResponse getProfile(String userID) {
        Profile profile = profileRepository.findByUserID(UUID.fromString(userID)).orElseThrow(
                () -> new IllegalArgumentException("Profile not found")
        );
        return modelMapper.map(profile, ProfileResponse.class);
    }

    public ProfileUpdateResponse updateProfile(String userID, String profileID, ProfileUpdateRequest request) {
        Profile retrievedProfile = profileRepository.findByProfileID(UUID.fromString(profileID)).orElseThrow(
                () -> new ResourceNotFoundException("Profile not found with ID: " + profileID)
        );

        if (!retrievedProfile.getUserID().toString().equals(userID)) {
            throw new SecurityException("User does not have permission to update this profile");
        }
        // Update only non-null fields
        if (request.getFirstName() != null) {
            retrievedProfile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            retrievedProfile.setLastName(request.getLastName());
        }
        if (request.getHeadline() != null) {
            retrievedProfile.setHeadline(request.getHeadline());
        }
        if (request.getSummary() != null) {
            retrievedProfile.setSummary(request.getSummary());
        }
        if (request.getLocation() != null) {
            retrievedProfile.setLocation(request.getLocation());
        }
        if (request.getPhone() != null) {
            retrievedProfile.setPhone(request.getPhone());
        }
        if (request.getProfilePictureURL() != null) {
            retrievedProfile.setProfilePictureURL(request.getProfilePictureURL());
        }
        if(request.isPublic() != retrievedProfile.isPublic()){
            retrievedProfile.setPublic(request.isPublic());
        }

        Profile savedProfile = profileRepository.save(retrievedProfile);
        return modelMapper.map(savedProfile, ProfileUpdateResponse.class);
    }

    public void deleteProfile(String userID, String profileID) {
        Profile retrievedProfile = profileRepository.findByProfileID(UUID.fromString(profileID)).orElseThrow(
                () -> new ResourceNotFoundException("Profile not found with ID: " + profileID)
        );

        if (!retrievedProfile.getUserID().toString().equals(userID)) {
            throw new SecurityException("User does not have permission to delete this profile");
        }

        profileRepository.delete(retrievedProfile);

    }

}
