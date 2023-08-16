package eshop.service;

import eshop.model.Category;
import eshop.model.Product;
import eshop.util.ProductNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAllProducts();

    List<Product> findRandomProducts(Integer quantity);

    List<Product> findProductsByCategory(Category category);

    Product findProductById(UUID id) throws ProductNotFoundException;

    List<Product> findByNamePart(String name);

    void saveProduct(Product product);

    void deleteProduct(Product product);

    Boolean isProductUnique(UUID id, String name);

    List<Product> findByCategoryAndNamePart(String name, Category category);
}
