package com.example.airspace.AirSpace.security.jwt;

import com.example.airspace.AirSpace.security.exceptionHandler.GlobalExceptionHandler;

import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.airspace.AirSpace.DTOs.OtpRequest;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import com.example.airspace.AirSpace.constants.*;;



@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    GlobalExceptionHandler g = new GlobalExceptionHandler();

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpiration}")
    private int jwtExpiration;

    @Autowired
    private UserDetailsService userDetailsService;

    public String generateToken(Authentication authentication) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signatureAlgorithm, jwtSecret)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateTokenForUser(String email, OtpPurpose purpose) {
        return Jwts.builder()
                .setSubject(email)  // User's email as subject
                .claim("purpose",purpose)   
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))  // Set expiration time
                .signWith(SignatureAlgorithm.HS256, jwtSecret)  // Use your secret key for signing
                .compact();
    }
    // Validate if the token is expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    // Extract claims from the token
    private Claims extractAllClaims(String token){
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


    }

    // Validate token with expected purpose
    public boolean validateTheToken(String token, String expectedPurpose) {
        try {
            Claims claims = extractAllClaims(token);

            String tokenPurpose = claims.get("purpose", String.class);
            return expectedPurpose.equals(tokenPurpose) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }



    public boolean validateToken(String token,String username) {
        try {
            return (username.equals(username));
        } catch (Exception e){
            System.out.println("validate Token"+e.getMessage());
            logger.error(e.getMessage());
        }
        return false;
    }


    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}
