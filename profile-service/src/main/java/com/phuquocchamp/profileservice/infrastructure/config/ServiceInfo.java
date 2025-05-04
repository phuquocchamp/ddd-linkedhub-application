package com.phuquocchamp.profileservice.infrastructure.config;

import com.phuquocchamp.profileservice.application.dto.ServiceInfoDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "")
@Getter
@Setter
public class ServiceInfo {
    private Map<String, String> build;
    private Map<String, String> application;

    @Bean
    public ServiceInfoDto serviceInfoDto() {
        ServiceInfoDto dto = new ServiceInfoDto();
        dto.setBuild(build);
        dto.setApplication(application);
        return dto;
    }
}
