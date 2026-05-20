package com.example.airspace.AirSpace.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    private final List<String> excludedPaths=List.of(
            "/auth/register",
            "/auth/login",
            "/auth/forgotPassword",
            "/auth/verifyPasswordOtp",
            "/auth/resetPassword"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String requestPath = request.getServletPath();

            // Skip filter for excluded paths
            if (excludedPaths.stream().anyMatch(requestPath::equalsIgnoreCase)) {
                System.out.println("Skipping JWT validation for path: " + requestPath);
                filterChain.doFilter(request, response);
                return;
            }

            // Extract and validate the token
            String token = getJwtFromRequest(request);
        try {
            String username = tokenProvider.extractUsername(token); // This will throw if the token is invalid or expired
            if (tokenProvider.validateToken(token, username)) {
                Authentication authentication = tokenProvider.getAuthentication(token);

                //  Set authentication in SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
//            else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.setContentType("application/json"); // Set content type to JSON
//                response.getWriter().write("{\"message\": \"Invalid or expired token\"}");
//                return;
//            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            // Handle other JWT-related exceptions (e.g., malformed token)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json"); // Set content type to JSON
            response.getWriter().write("{\"message\": \"Invalid or expired token\"}");
            return;
        }
        // Continue with the rest of the filter chain
            filterChain.doFilter(request, response);

    }
//  Read the Authorization header from the HTTP request
    private String getJwtFromRequest(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader("Authorization");
//  Check if the header is present and starts with "Bearer "
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
                return bearerToken.substring(7);  // Extract the token (remove "Bearer " prefix)
            }
        }catch (Exception e){
            logger.error(e);
        }
        return null;
    }


}
