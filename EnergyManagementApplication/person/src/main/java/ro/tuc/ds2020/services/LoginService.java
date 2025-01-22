package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.LoginRequestDTO;
import ro.tuc.ds2020.dtos.LoginResponseDTO;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.security.JWTGenerator;

import java.util.List;

@Service
public class LoginService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final RestTemplate restTemplate;


    @Autowired
    public LoginService(PersonRepository personRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator, RestTemplate restTemplate) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.restTemplate = restTemplate;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        Person user = personRepository.findByName(loginRequest.getUsername())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String redirectUrl = user.getRole().equalsIgnoreCase("admin")
                ? "/admin/dashboard"
                : "/client/dashboard";

        String token = jwtGenerator.generateToken(user.getName());

        //String url = "http://localhost:8081/api/devices/user/" + user.getId();
        String url = "http://reverse-proxy/device/api/devices/user/" + user.getId();
        List<DeviceDTO> devices = List.of();
        try {
            DeviceDTO[] fetchedDevices = restTemplate.getForObject(url, DeviceDTO[].class);
            if (fetchedDevices != null) {
                devices = List.of(fetchedDevices);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch devices for user: " + e.getMessage());
        }

        return new LoginResponseDTO(user.getRole(), redirectUrl, user.getId(), token, devices);
    }

}
