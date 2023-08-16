package eshop.service.implementation;

import eshop.repository.ProductRepository;
import eshop.service.ProductService;
import eshop.model.Category;
import eshop.model.Product;
import eshop.util.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findRandomProducts(Integer quantity) {
        List<Product> products = productRepository.findAll();
        Collections.shuffle(products);
        return products.stream().limit(quantity).collect(Collectors.toList());
    }

    @Override
    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product findProductById(UUID id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
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
        return productRepository.findByName(name)
            .map(product -> product.getId().equals(id))
            .orElse(true);
    }

    @Override
    public List<Product> findByCategoryAndNamePart(String name, Category category) {
        return productRepository.findByNameContaining(name)
            .stream()
            .filter(p -> category == null || p.getCategory().equals(category))
            .collect(Collectors.toList());
    }
}
