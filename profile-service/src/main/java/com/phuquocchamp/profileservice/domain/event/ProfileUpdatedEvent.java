package com.phuquocchamp.profileservice.domain.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileUpdatedEvent {
    private final String userId;
    private final String name;
    private final String email;
}
