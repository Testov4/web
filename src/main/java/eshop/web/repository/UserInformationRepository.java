package eshop.web.repository;

import eshop.web.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {
}
