package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceAssignmentDTO;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.dtos.UserSyncDTO;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.security.JWTGenerator;
import org.springframework.http.HttpHeaders;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final RestTemplate restTemplate;
    private final PersonBuilder personBuilder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    public PersonService(PersonRepository personRepository, RestTemplate restTemplate, PersonBuilder personBuilder, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.restTemplate = restTemplate;
        this.personBuilder = personBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID addPerson(PersonDetailsDTO personDTO) {
        Person person = new Person(
                personDTO.getName(),
                personDTO.getAddress(),
                personDTO.getAge(),
                personDTO.getRole(),
                passwordEncoder.encode(personDTO.getPassword())
        );

        Person savedPerson = personRepository.save(person);

        UserSyncDTO userSyncDTO = new UserSyncDTO(savedPerson.getId(), savedPerson.getName());
        syncUserWithDeviceMicroservice(userSyncDTO);

        return savedPerson.getId();
    }

    public void updatePerson(UUID personId, PersonDetailsDTO personDTO) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + personId));

        updatePersonDetails(person, personDTO);
        personRepository.save(person);

        UserSyncDTO userSyncDTO = new UserSyncDTO(personId, personDTO.getName());
        restTemplate.put("http://reverse-proxy/device/api/users/" + personId, userSyncDTO);
        //restTemplate.put("http://localhost:8081/api/users/" + personId, userSyncDTO);

    }

    public List<PersonDTO> findPersons() {
        return personRepository.findAll().stream()
                .map(personBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findPersonById(UUID personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + personId));
        return personBuilder.toPersonDTO(person);
    }

    public void deletePerson(UUID personId) {
        if (!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("Person not found with id: " + personId);
        }
        personRepository.deleteById(personId);
        deleteUserFromDeviceMicroservice(personId);
    }

    public void assignDeviceToUser(UUID userId, UUID deviceId) {
        String url = "http://reverse-proxy/device/api/devices/assign";
        //String url = "http://localhost:8081/api/devices/assign";

        try {
            restTemplate.postForObject(url, new DeviceAssignmentDTO(userId, deviceId), String.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to assign device to user: " + e.getMessage());
        }
    }

    private void updatePersonDetails(Person person, PersonDetailsDTO personDTO) {
        person.setName(personDTO.getName());
        person.setAddress(personDTO.getAddress());
        person.setAge(personDTO.getAge());
        person.setRole(personDTO.getRole());
        if (personDTO.getPassword() != null && !personDTO.getPassword().isEmpty()) {
            person.setPassword(passwordEncoder.encode(personDTO.getPassword()));
        }
    }

    private void deleteUserFromDeviceMicroservice(UUID personId) {
        try {
            restTemplate.delete("http://reverse-proxy/device/api/users/" + personId);
            //restTemplate.delete("http://localhost:8081/api/users/" + personId);

            System.out.println("User deleted successfully from device microservice.");
        } catch (Exception e) {
            System.err.println("Failed to delete user from device microservice: " + e.getMessage());
        }
    }

    private void syncUserWithDeviceMicroservice(UserSyncDTO userSyncDTO) {
        try {
            System.out.println("Syncing user to device microservice: " + userSyncDTO);

            String token = jwtGenerator.generateToken(userSyncDTO.getName());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UserSyncDTO> entity = new HttpEntity<>(userSyncDTO, headers);

            //String response = restTemplate.postForObject("http://localhost:8081/api/users/sync", entity, String.class);
            String response = restTemplate.postForObject("http://reverse-proxy/device/api/users/sync", entity, String.class);

            System.out.println("Response from device microservice: " + response);
        } catch (Exception e) {
            System.err.println("Error syncing user: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
