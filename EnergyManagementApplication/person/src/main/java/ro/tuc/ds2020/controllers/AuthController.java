package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.security.JWTGenerator;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private PersonRepository personRepository;



    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        String publicKey = Base64.getEncoder().encodeToString(jwtGenerator.getSigningKey().getEncoded());
        System.out.println("Public key returned: " + publicKey);
        return ResponseEntity.ok(publicKey);
    }


    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            System.out.println("Token received for validation: " + authorizationHeader);
            String token = authorizationHeader.replace("Bearer ", "");
            boolean isValid = jwtGenerator.validateToken(token);
            System.out.println("Token validation result: " + isValid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/validate/{userId}")
    public ResponseEntity<Void> validateUser(@PathVariable String userId) {
        if ("admin".equals(userId)) {
            return ResponseEntity.ok().build();
        }

        try {
            UUID userUUID = UUID.fromString(userId);
            boolean userExists = personRepository.existsById(userUUID);
            if (userExists) {
                return ResponseEntity.ok().build();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid userId format: " + userId);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
