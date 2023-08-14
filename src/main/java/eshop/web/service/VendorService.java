package eshop.web.service;

import eshop.web.model.Vendor;
import eshop.web.util.UniqueStatus;
import java.util.List;
import java.util.UUID;

public interface VendorService {
    List<Vendor> findAllVendors();

    Vendor findVendorById(UUID id);

    void saveVendor(Vendor vendor);

    void deleteVendor(Vendor vendor);

    UniqueStatus isVendorUnique(UUID id, String name);
}
