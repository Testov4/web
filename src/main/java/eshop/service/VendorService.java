package eshop.service;

import eshop.model.Vendor;
import eshop.exception.UserNotFoundException;
import java.util.List;
import java.util.UUID;

public interface VendorService {
    List<Vendor> findAllVendors();

    Vendor findVendorById(UUID id) throws UserNotFoundException;

    void saveVendor(Vendor vendor);

    void deleteVendor(Vendor vendor);

    Boolean isVendorUnique(UUID id, String name);
}
