package eshop.service.implementation;

import eshop.repository.VendorRepository;
import eshop.model.Vendor;
import eshop.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;

    @Override
    public List<Vendor> findAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor findVendorById(UUID id) {
        return vendorRepository.findById(id).orElse(null);
    }

    @Override
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
    public Boolean isVendorUnique(UUID id, String name) {
        return vendorRepository.findByName(name)
            .map(product -> product.getId().equals(id))
            .orElse(true);
    }
}
