package eshop.web.services;


import eshop.web.models.Category;
import eshop.web.models.Product;
import eshop.web.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PublicKey;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findRandomItems(int quantity) {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty())
            return null;
        Collections.shuffle(products);
        if (quantity > products.size())
            return products.subList(0, products.size());
        return products.subList(0, quantity);
    }

    public List<Product> findByCategory(Category category) {
        List<Product> products = productRepository.findByCategory(category).orElse(null);
        return products;
    }

    public Product findOne(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> findForSearch(String name) {
        return productRepository.findByNameContaining(name).orElse(null);
    }

    @Transactional
    public void save(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }

    public String isProductUnique(Integer id, String name) {
        Product foundProduct = productRepository.findByName(name);
        boolean productIsNew = (id == null);
        if (productIsNew) {
            if (foundProduct != null)
                return "Duplicate";
        }
        if (foundProduct!=null) {
            if (foundProduct.getId()!=id){
                return "Duplicate";
            }
        }
        return "OK";
    }

    public List<Product> findForSearchWithCategory(String name, Category category) {
        List<Product> products = productRepository.findByNameContaining(name).orElse(null);
        if (products==null)
            return null;
        List<Product> productsWithCategory = products.stream().filter(p ->(p.getCategory()
                .equals(category))).collect(Collectors.toList());
        return productsWithCategory;
    }

}
