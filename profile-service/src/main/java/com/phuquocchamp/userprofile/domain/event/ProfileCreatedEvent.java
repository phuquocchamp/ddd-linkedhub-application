package com.phuquocchamp.userprofile.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileCreatedEvent {
    private final String userId;
    private final String name;
    private final String email;
}
