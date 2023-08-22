package eshop.repository;

import eshop.model.Category;
import eshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategory(Category category);

    List<Product> findByNameContaining(String name);

    Optional<Product> findByName(String name);

    @Query(nativeQuery=true, value="SELECT * FROM product ORDER BY random() LIMIT 4")
    List<Product> findFourRandomProducts();
}
