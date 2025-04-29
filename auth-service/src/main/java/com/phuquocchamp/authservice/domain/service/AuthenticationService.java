package com.phuquocchamp.authservice.domain.service;

import com.phuquocchamp.authservice.application.dto.request.AuthenticationRequest;
import com.phuquocchamp.authservice.application.dto.response.AuthenticationResponse;
import com.phuquocchamp.authservice.application.dto.response.TokenResponse;
import com.phuquocchamp.authservice.domain.model.aggregate_root.User;
import com.phuquocchamp.authservice.domain.model.entity.RefreshToken;
import com.phuquocchamp.authservice.domain.repository.RefreshTokenRepository;
import com.phuquocchamp.authservice.domain.repository.UserRepository;
import com.phuquocchamp.authservice.infrastructure.utils.EmailService;
import com.phuquocchamp.authservice.infrastructure.utils.JwtTokenProvider;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class AuthenticationService {
    private static final int REFRESH_TOKEN_HOURS = 24;
    private static final int TOKEN_LENGTH = 6;
    private static final int TOKEN_DURATION_MINUTES = 1;


    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationService(EmailService emailService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public TokenResponse register(AuthenticationRequest request) throws MessagingException, UnsupportedEncodingException {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exits");
        }
        User user = userRepository.save(new User(request.getEmail(), passwordEncoder.encode(request.getPassword())));
        String verifyToken = jwtTokenProvider.generateRandomToken(TOKEN_LENGTH);

        // Update Verify Token
        String hashedToken = passwordEncoder.encode(verifyToken);
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(TOKEN_DURATION_MINUTES);
        user.setEmailVerificationToken(hashedToken);
        user.setEmailVerificationExpiryDate(expiryDate);

        emailService.sendEmail(user.getEmail(), "Email Verification", emailVerificationContent(verifyToken));
        String authToken = jwtTokenProvider.generateToken(user.getUserID().toString(), user.getEmail());

        // persist refresh token
        String token = jwtTokenProvider.generateRefreshToken();
        RefreshToken refreshToken = new RefreshToken(token, user.getUserID().toString(), LocalDateTime.now().plusHours(REFRESH_TOKEN_HOURS), false);
        refreshTokenRepository.save(refreshToken);

        return new TokenResponse(authToken, token);
    }

    public TokenResponse login(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }
        String authToken = jwtTokenProvider.generateToken(user.getUserID().toString(), user.getEmail());
         // persist refresh token
        String reToken = jwtTokenProvider.generateRefreshToken();
        RefreshToken refreshToken = new RefreshToken(reToken, user.getUserID().toString(), LocalDateTime.now().plusHours(REFRESH_TOKEN_HOURS), false);
        refreshTokenRepository.save(refreshToken);

        return new TokenResponse(authToken, reToken);
    }

    public TokenResponse refreshToken(String refreshToken) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow(
                () -> new IllegalArgumentException("Invalid refresh token")
        );
        if (token.isExpired()) {
            throw new IllegalArgumentException("Refresh token is expired");
        }
        if (token.isRevoked()) {
            throw new IllegalArgumentException("Refresh token is revoked");
        }

        User user = userRepository.findByUserID(UUID.fromString(token.getUserID())).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        String newAccessToken = jwtTokenProvider.generateToken(user.getUserID().toString(), user.getEmail());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken();

        RefreshToken newToken = new RefreshToken(newRefreshToken, token.getUserID(), LocalDateTime.now().plusHours(REFRESH_TOKEN_HOURS), false);
        refreshTokenRepository.save(newToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void revokeRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow(
                () -> new IllegalArgumentException("Invalid refresh token")
        );
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    public void sendEmailVerificationToken(String email) throws MessagingException, UnsupportedEncodingException {
        User user = getUserDetails(email);
        if (user.getEmailVerified()) {
            throw new IllegalArgumentException("Email already verified");
        }
        String token = jwtTokenProvider.generateRandomToken(TOKEN_LENGTH);
        String hashedToken = passwordEncoder.encode(token);
        LocalDateTime retrievedExpirationDate = LocalDateTime.now().plusMinutes(TOKEN_DURATION_MINUTES);

        user.setEmailVerificationToken(hashedToken);
        user.setEmailVerificationExpiryDate(retrievedExpirationDate);

        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), "Email Verification", emailVerificationContent(token));
    }

    public void validateEmailVerificationToken(String token, String email) {
        User user = getUserDetails(email);

        String retrievedToken = user.getEmailVerificationToken();
        LocalDateTime retrievedExpirationDate = user.getEmailVerificationExpiryDate();

        if (!passwordEncoder.matches(token, retrievedToken)) {
            throw new IllegalArgumentException("Invalid token");
        }
        if (retrievedExpirationDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token is expired");
        }

        user.setIsActive(true);
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiryDate(null);

        userRepository.save(user);
    }

    public User getUserDetails(String email) {

        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User does not exist"));
    }

    private String emailVerificationContent(String token) {
        return String.format("""
                        Only one step to take full advantage of LinkedHub.
                        
                        Enter this code to verify your email: %s. The code will expire in %s minutes.""",
                token, TOKEN_DURATION_MINUTES);
    }

}
