package eshop.repository;

import eshop.model.User;
import eshop.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {
    void deleteByUser(User user);

    Optional<UserInformation> findByUser(User user);
}
