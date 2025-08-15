package com.clinicmanagement.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

/**
 * TokenService: generates and validates JWT tokens.
 *
 * Requirements satisfied:
 * - generateToken(email): creates a JWT with issuedAt & expiration using Jwts.builder()
 * - getSigningKey(): derives a Key from configured secret
 * - validate/isValid methods to check token validity & subject
 *
 * NOTE: add the jjwt dependencies to your pom.xml:
 *
 * <dependency>
 *   <groupId>io.jsonwebtoken</groupId>
 *   <artifactId>jjwt-api</artifactId>
 *   <version>0.11.5</version>
 * </dependency>
 * <dependency>
 *   <groupId>io.jsonwebtoken</groupId>
 *   <artifactId>jjwt-impl</artifactId>
 *   <version>0.11.5</version>
 *   <scope>runtime</scope>
 * </dependency>
 * <dependency>
 *   <groupId>io.jsonwebtoken</groupId>
 *   <artifactId>jjwt-jackson</artifactId>
 *   <version>0.11.5</version>
 *   <scope>runtime</scope>
 * </dependency>
 */
@Service
public class TokenService {

    // Base64-encoded secret (recommended). You can also provide a plain long random string.
    @Value("${app.jwt.secret:changeitchangitchangitchangit!changeit}") // override in application.properties
    private String secret;

    // Token validity in milliseconds (default: 1 hour)
    @Value("${app.jwt.expiration-ms:3600000}")
    private long expirationMs;

    // Derived signing key
    private Key signingKey;

    @PostConstruct
    public void init() {
        // If secret is not Base64, make it so (this ensures sufficient entropy)
        if (!isBase64(secret)) {
            secret = Base64.getEncoder().encodeToString(secret.getBytes());
        }
        signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    /**
     * Generate a JWT token for the given subject (e.g., user's email).
     *
     * @param subject (email or username)
     * @return compact JWT string
     */
    public String generateToken(String subject) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiry = Date.from(now.plusMillis(expirationMs));

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)                     // issuedAt
                .setExpiration(expiry)                     // expiration
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate token and optionally check that the subject matches expectedSubject.
     *
     * @param token           JWT token string
     * @param expectedSubject (optional) expected subject (email); pass null to skip subject check
     * @return true if valid and not expired (and subject matches when provided)
     */
    public boolean isValid(String token, String expectedSubject) {
        try {
            Claims claims = parseClaims(token);
            if (expectedSubject != null && !expectedSubject.equals(claims.getSubject())) {
                return false;
            }
            // JJWT already checks expiration in parseClaims — if no exception thrown, token is valid
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // token invalid / expired / malformed / unsupported / signature failed
            return false;
        }
    }

    /**
     * Extract the subject (e.g., email) from the token. Returns null on failure.
     */
    public String extractSubject(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Retrieve the signing key used internally (useful for tests or other components).
     */
    public Key getSigningKey() {
        return signingKey;
    }

    // Helper: parse claims with the signing key; will throw JwtException subclasses on failure
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Simple base64 detection
    private boolean isBase64(String s) {
        try {
            Base64.getDecoder().decode(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
