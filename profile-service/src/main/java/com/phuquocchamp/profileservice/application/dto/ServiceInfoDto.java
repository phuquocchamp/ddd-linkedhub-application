package com.phuquocchamp.profileservice.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class ServiceInfoDto {
    private Map<String, String> build;
    private Map<String, String> application;
}
