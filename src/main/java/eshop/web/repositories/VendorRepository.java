package eshop.web.repositories;

import eshop.web.models.User;
import eshop.web.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    public Vendor findByName(String name);
}
