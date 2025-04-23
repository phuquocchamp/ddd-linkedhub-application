package com.phuquocchamp.profileservice.application.dto;

public record CreateProfileRequest(String userId, String name, String email) {
}