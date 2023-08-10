package eshop.web.services;

import eshop.web.models.Vendor;
import eshop.web.repositories.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    @Autowired
    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public List<Vendor> findAll(){
        return vendorRepository.findAll();
    }

    public Vendor findOne(int id){
        return vendorRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Vendor vendor){
        vendorRepository.save(vendor);
    }

    @Transactional
    public void delete(Vendor vendor){
        vendorRepository.delete(vendor);
    }

    public String isVendorUnique(Integer id, String name){
        Vendor foundVendor = vendorRepository.findByName(name);
        boolean vendorIsNew = (id == null);
        if (vendorIsNew) {
            if (foundVendor != null)
                return "Duplicate";
        }
        if (foundVendor!=null) {
            if (foundVendor.getId()!=id){
                return "Duplicate";
            }
        }
        return "OK";
    }
}
