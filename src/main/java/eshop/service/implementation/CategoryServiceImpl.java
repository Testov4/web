package eshop.service.implementation;

import eshop.model.Category;
import eshop.repository.CategoryRepository;
import eshop.service.CategoryService;
import eshop.util.CategoryNotFoundException;
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
    public List<Category> findSubCategoriesByParentId(UUID id) throws CategoryNotFoundException {
        return categoryRepository.findByParent(categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found")));
    }

    @Override
    public List<Category> findAllSubCategories() {
        return findAllCategories().stream()
            .filter(p -> p.getParent() != null)
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
        categoryRepository.save(category);
        log.info("Category {} successfully created", category);
    }

    @Override
    @Transactional
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }
}
