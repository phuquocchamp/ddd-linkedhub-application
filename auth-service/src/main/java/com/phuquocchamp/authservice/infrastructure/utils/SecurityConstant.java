package com.phuquocchamp.authservice.infrastructure.utils;

public interface SecurityConstant {
    String[] INSECURE_ENDPOINTS = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/reset-password",
            "/api/v1/auth/send-password-reset-token",
            "/api/v1/auth/public/**"
    };
}
