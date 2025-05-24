package com.vetapi.infrastructure.security;

import com.vetapi.domain.entity.User;
import com.vetapi.domain.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // If no Authorization header or doesn't start with Bearer, continue with filter chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token from Authorization header
        jwt = authHeader.substring(7);
        
        try {
            userEmail = jwtService.extractEmail(jwt);

            // If email exists and user is not already authenticated
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Validate token
                if (jwtService.isTokenValid(jwt)) {
                    // Find user in database
                    Optional<User> userOptional = userRepository.findByEmail(userEmail);
                    
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        
                        // Create authentication token with user details
                        List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole())
                        );
                        
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            authorities
                        );
                        
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        
                        log.debug("User {} authenticated successfully with role {}", userEmail, user.getRole());
                    } else {
                        log.warn("User not found in database: {}", userEmail);
                    }
                } else {
                    log.warn("Invalid or expired JWT token for user: {}", userEmail);
                }
            }
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Don't filter auth endpoints, debug endpoints, swagger, actuator
        return path.startsWith("/api/auth/") || 
               path.startsWith("/api/debug/") ||
               path.startsWith("/api/v3/api-docs/") ||
               path.startsWith("/api/swagger-ui/") ||
               path.startsWith("/api/actuator/health");
    }
} 