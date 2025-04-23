package com.phuquocchamp.profileservice.domain.model.value_object;

import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public class Email {
    private String value;

    public Email() {
    }

    public Email(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.value = value;
    }

    private boolean isValid(String value) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(value).matches();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return value.equals(email.value);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
