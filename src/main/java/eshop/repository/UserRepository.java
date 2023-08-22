package eshop.repository;

import eshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsernameAndIdNot(String username, UUID id);

    Boolean existsByEmailAndIdNot(String email, UUID id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
