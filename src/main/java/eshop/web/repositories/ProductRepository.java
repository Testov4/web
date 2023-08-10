package eshop.web.repositories;

import eshop.web.models.Category;
import eshop.web.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Optional<List<Product>> findByCategory(Category category);

    public Optional<List<Product>> findByNameContaining(String name);

    public Product findByName(String name);

}
