package eshop.web.service;

import eshop.web.model.Category;
import eshop.web.model.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAllProducts();

    List<Product> findRandomProducts(int quantity);

    List<Product> findProductsByCategory(Category category);

    Product findProductById(UUID id);

    List<Product> findByNamePart(String name);

    void saveProduct(Product product);

    void deleteProduct(Product product);

    Boolean isProductUnique(UUID id, String name);

    List<Product> findByCategoryAndNamePart(String name, Category category);
}
