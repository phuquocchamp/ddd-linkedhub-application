package com.phuquocchamp.authservice.application.api;

import com.phuquocchamp.authservice.application.dto.request.AuthenticationRequest;
import com.phuquocchamp.authservice.application.dto.request.RefreshTokenRequest;
import com.phuquocchamp.authservice.application.dto.response.AuthenticationResponse;
import com.phuquocchamp.authservice.application.dto.response.TokenResponse;
import com.phuquocchamp.authservice.domain.model.aggregate_root.User;
import com.phuquocchamp.authservice.domain.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // login
    // register
    // refresh token
    // revoke token

    @GetMapping("/public/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome to Auth Service");
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody AuthenticationRequest request) throws MessagingException, UnsupportedEncodingException {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.login(request), HttpStatus.OK);
    }

    @PutMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse response = authenticationService.refreshToken(request.getRefreshToken());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/revoke-token")
    public ResponseEntity<Void> revoke(@Valid @RequestBody RefreshTokenRequest request) {
        authenticationService.revokeRefreshToken(request.getRefreshToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserDetails() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = authenticationService.getUserDetails(userDetails.getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/send-email-verification-token")
    public ResponseEntity<String> sendEmailVerificationToken() throws MessagingException, UnsupportedEncodingException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authenticationService.sendEmailVerificationToken(userDetails.getUsername());
        return new ResponseEntity<>("Email verification token sent successfully", HttpStatus.OK);
    }

    @PutMapping("/validate-email-verification-token")
    public ResponseEntity<String> validateEmailVerificationToken(@RequestParam String token) throws MessagingException, UnsupportedEncodingException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authenticationService.validateEmailVerificationToken(token, userDetails.getUsername());
        return new ResponseEntity<>("Email verification token validated successfully", HttpStatus.OK);
    }


}
