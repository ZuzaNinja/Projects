package ro.tuc.ds2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tuc.ds2020.entities.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
