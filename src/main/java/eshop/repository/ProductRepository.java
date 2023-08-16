package eshop.repository;

import eshop.model.Category;
import eshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategory(Category category);

    List<Product> findByNameContaining(String name);

    Optional<Product> findByName(String name);

    Optional<Product> findByNameIsAndIdNot(String name, UUID id);
}
