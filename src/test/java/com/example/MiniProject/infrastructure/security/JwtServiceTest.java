package com.example.MiniProject.infrastructure.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        // Inject values for the @Value fields
        ReflectionTestUtils.setField(jwtService, "secret", "mySecretKey12345678901234567890123456789012");
        ReflectionTestUtils.setField(jwtService, "expiration", 3600000L); // 1 hour in milliseconds
    }

    @Test
    void testGenerateToken() {
        String email = "test@example.com";
        String token = jwtService.genrateToken(email);

        assertNotNull(token);


        Key key = Keys.hmacShaKeyFor("mySecretKey12345678901234567890123456789012".getBytes());
        String subject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertEquals(email, subject);
    }
}

