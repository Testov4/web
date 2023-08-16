package eshop.service;

import eshop.model.Category;
import eshop.util.CategoryNotFoundException;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<Category> findAllCategories();

    List<Category> findMainCategories();

    List<Category> findSubCategoriesByParentId(UUID id) throws CategoryNotFoundException;

    List<Category> findAllSubCategories();

    Category findCategoryById(UUID id);

    void saveCategory(Category category);

    void deleteCategory(Category category);

}
