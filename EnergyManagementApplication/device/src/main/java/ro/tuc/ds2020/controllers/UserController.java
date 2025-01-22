package ro.tuc.ds2020.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.UserSyncDTO;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncUser(@RequestBody UserSyncDTO userSyncDTO) {
        System.out.println("Received user sync request: " + userSyncDTO);
        userService.addUserFromPersonService(userSyncDTO);
        System.out.println("User synchronized: " + userSyncDTO.getId());
        return ResponseEntity.ok("User synchronized successfully.");
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody UserSyncDTO userSyncDTO) {
        userService.updateUserFromPersonService(id, userSyncDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        LOGGER.info("Received DELETE request in User service for ID {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
