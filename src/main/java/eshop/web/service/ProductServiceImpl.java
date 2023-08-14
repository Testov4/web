package eshop.web.service;

import eshop.web.model.Category;
import eshop.web.model.Product;
import eshop.web.repository.ProductRepository;
import eshop.web.util.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findRandomProducts(int quantity) {
        List<Product> products = productRepository.findAll();
        Collections.shuffle(products);
        if (quantity > products.size()) {
            return products.subList(0, products.size());
        }
        return products.subList(0, quantity);
    }

    @Override
    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product findProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Override
    public List<Product> findByNamePart(String name) {
        return productRepository.findByNameContaining(name);
    }

    @Override
    @Transactional
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public Boolean isProductUnique(UUID id, String name) {
        log.info("checking if product is unique for name: {} and ID: {}", name, id);
        Product foundProduct = productRepository.findByName(name);
        boolean productIsNew = (id == null);

        if (productIsNew && foundProduct != null) {
            log.info("product is not unique, returning false, found product: {}", foundProduct);
            return false;
        }
        if (foundProduct != null && foundProduct.getId() != id) {
            log.info("product is not unique, returning false, found product: {}", foundProduct);
            return false;
        }
        log.info("product is unique, returning true");
        return true;
    }

    @Override
    public List<Product> findByCategoryAndNamePart(String name, Category category) {
        log.info("selecting product by category: {} and part of the name: {}", name, category);
        List<Product> products = productRepository.findByNameContaining(name);
        return products.stream()
            .filter(p -> category == null || p.getCategory().equals(category))
            .collect(Collectors.toList());
    }
}
