package eshop.web.service;

import eshop.web.model.Vendor;
import eshop.web.repository.VendorRepository;
import eshop.web.util.UniqueStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService{
    private final VendorRepository vendorRepository;

    @Override
    public List<Vendor> findAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor findVendorById(UUID id) {
        return vendorRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveVendor(Vendor vendor) {
        vendorRepository.save(vendor);
    }

    @Override
    @Transactional
    public void deleteVendor(Vendor vendor) {
        vendorRepository.delete(vendor);
    }

    @Override
    public UniqueStatus isVendorUnique(UUID id, String name) {
        log.info("checking if vendor is unique for name: {}", name);
        Vendor foundVendor = vendorRepository.findByName(name);
        boolean vendorIsNew = (id == null);
        if (vendorIsNew && (foundVendor != null)) {
            log.info("vendor is not unique, found another vendor with the same name: {}", name);
            return UniqueStatus.DUPLICATE;
        }
        if (foundVendor!=null) {
            if (foundVendor.getId() != id){
                log.info("vendor is not unique, found another vendor with the same name: {}", name);
                return UniqueStatus.DUPLICATE;
            }
        }

        log.info("vendor is unique");
        return UniqueStatus.OK;
    }
}
