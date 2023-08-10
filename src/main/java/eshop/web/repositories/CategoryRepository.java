package eshop.web.repositories;


import eshop.web.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    public List<Category> findByParentNull();

    public List<Category> findByParent(Category category);
}
