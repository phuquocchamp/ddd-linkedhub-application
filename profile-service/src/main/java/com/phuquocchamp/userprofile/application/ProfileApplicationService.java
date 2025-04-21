package com.phuquocchamp.userprofile.application;

import com.phuquocchamp.userprofile.domain.event.ProfileUpdatedEvent;
import com.phuquocchamp.userprofile.domain.model.aggregate_root.Profile;
import com.phuquocchamp.userprofile.domain.model.entity.Education;
import com.phuquocchamp.userprofile.domain.model.value_object.DateRange;
import com.phuquocchamp.userprofile.domain.model.value_object.Email;
import com.phuquocchamp.userprofile.domain.repository.ProfileRepository;
import com.phuquocchamp.userprofile.domain.service.ProfileValidationService;
import com.phuquocchamp.userprofile.infrastructure.messaging.KafkaEventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProfileApplicationService {
    private final ProfileRepository repository;
    private final ProfileValidationService validationService;
    private final KafkaEventPublisher eventPublisher;

    public ProfileApplicationService(ProfileRepository repository,
                                     ProfileValidationService validationService,
                                     KafkaEventPublisher eventPublisher) {
        this.repository = repository;
        this.validationService = validationService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void createProfile(String userId, String name, String email) {
        Email emailObj = new Email(email);
//        validationService.validateEmailUniqueness(emailObj, null);

        Profile profile = new Profile(userId, name, emailObj);
        repository.save(profile);

//        eventPublisher.publish(new ProfileCreatedEvent(userId, name, email));
    }

    @Transactional
    public void updateProfile(String userId, String name, String email) {
        Profile profile = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Email emailObj = new Email(email);
        validationService.validateEmailUniqueness(emailObj, userId);

        profile.setName(name);
        profile.setEmail(emailObj);
        repository.save(profile);

        eventPublisher.publish(new ProfileUpdatedEvent(userId, name, email));
    }

    @Transactional
    public void addEducation(String userId, String school, String degree, String fieldOfStudy,
                             LocalDate startDate, LocalDate endDate) {
        Profile profile = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Education education = new Education(school, degree, fieldOfStudy,
                new DateRange(startDate, endDate));
        profile.addEducation(education);
        repository.save(profile);
    }

    public Profile getProfile(String userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
    }
}
