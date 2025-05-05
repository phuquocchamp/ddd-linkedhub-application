package com.phuquocchamp.profileservice.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "info")
@RefreshScope
@Getter @Setter
@JsonIgnoreProperties({"targetSource", "beanFactory", "beanExpressionResolver"})
public class ServiceInfoDto {
    private Map<String, String> build;
    private Map<String, String> contact;
}
