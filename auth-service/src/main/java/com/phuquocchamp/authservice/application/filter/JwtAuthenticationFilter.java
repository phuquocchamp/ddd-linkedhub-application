package com.phuquocchamp.authservice.application.filter;

import com.phuquocchamp.authservice.infrastructure.utils.JwtTokenProvider;
import com.phuquocchamp.authservice.infrastructure.utils.SecurityConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String AUTH_TOKEN = request.getHeader("Authorization");
        if (AUTH_TOKEN == null || !AUTH_TOKEN.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Skip token processing if no valid header
            return;
        }
        String TOKEN = AUTH_TOKEN.substring(7);

        try {
            if (!jwtTokenProvider.validateToken(TOKEN)) {
               response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                return;
            }

            if (jwtTokenProvider.isTokenExpired(TOKEN)) {
                 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired");
                return;
            }

            String email = jwtTokenProvider.getUserEmailFromToken(TOKEN);

            if (email != null) {
                UserDetails userDetails = new User(email, "", Collections.emptyList());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new ServletException("Error processing JWT Token", e);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return Arrays.stream(SecurityConstant.INSECURE_ENDPOINTS)
            .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
