package eshop.web.service;

import eshop.web.model.Category;
import eshop.web.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findMainCategories() {
        return categoryRepository.findByParentNull();
    }

    @Override
    public List<Category> findSubCategoriesByParentId(UUID id) {
        Category parent = categoryRepository.findById(id).orElse(null);
        return categoryRepository.findByParent(parent);
    }

    @Override
    public List<Category> findAllSubCategories() {
        return findAllCategories().stream()
            .filter(p -> (p.getParent() != null))
            .collect(Collectors.toList());
    }

    @Override
    public Category findCategoryById(UUID id){
        return categoryRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void saveCategory(Category category) {
        log.info("Creating new category: {}", category);
        if (category.getParent().getId() == null) {
            category.setParent(null);
        }
        categoryRepository.save(category);
        log.info("Category {} succesfully created", category);
    }

    @Override
    @Transactional
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }
}
