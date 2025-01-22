package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.UserRepository;
import ro.tuc.ds2020.dtos.UserSyncDTO;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a specific user by ID.
     */
    public User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void addUserFromPersonService(UserSyncDTO userSyncDTO) {
        UUID userId = userSyncDTO.getId();
        User user = userRepository.findById(userId)
                .orElse(new User(userId, userSyncDTO.getName()));
        user.setName(userSyncDTO.getName());
        userRepository.save(user);
    }

    public void updateUserFromPersonService(UUID id, UserSyncDTO userSyncDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setName(userSyncDTO.getName());
        userRepository.save(user);
    }

    /**
     * Deletes a user from the user table.
     */
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
