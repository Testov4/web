package eshop.web.service;

import eshop.web.model.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> findAllCategories();

    List<Category> findMainCategories();

    List<Category> findSubCategoriesByParentId(UUID id);

    List<Category> findAllSubCategories();

    Category findCategoryById(UUID id);

    void saveCategory(Category category);

    void deleteCategory(Category category);

}
