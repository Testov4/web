package eshop.service;

import eshop.model.Category;
import eshop.model.Product;
import eshop.exception.ProductNotFoundException;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAllProducts();

    List<Product> findRandomProducts();

    List<Product> findProductsByCategory(Category category);

    Product findProductById(UUID id) throws ProductNotFoundException;

    List<Product> findByNameContaining(String name);

    void saveProduct(Product product);

    void deleteProduct(Product product);

    Boolean isProductUnique(UUID id, String name);

    List<Product> findByCategoryAndNameContaining(String name, Category category);
}
