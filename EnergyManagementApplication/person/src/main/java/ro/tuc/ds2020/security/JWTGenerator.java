package ro.tuc.ds2020.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JWTGenerator {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final ConcurrentHashMap<String, String> tokenCache = new ConcurrentHashMap<>();


    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .claim("authorities", "SERVICE_ACCESS")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();

        tokenCache.put(username, token);
        return token;
    }


    public boolean validateToken(String token) {
        try {
            System.out.println("Validating token: " + token);
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            System.out.println("Token is valid");
            return true;
        } catch (Exception ex) {
            System.err.println("JWT validation failed: " + ex.getMessage());
            return false;
        }
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Key getSigningKey() {
        return key;
    }
}
