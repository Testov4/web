package eshop.web.services;


import eshop.web.models.Category;
import eshop.web.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    //Категории у которых нет категории родителя
    public List<Category> findMainCategories(){
        return categoryRepository.findByParentNull();
    }

    //Саб категории по айди родителя
    public List<Category> findSubCategories(int id){
        return categoryRepository.findByParent(categoryRepository.findById(id).orElse(null));
    }

    public List<Category> findAllSubCategories(){
        List<Category> categories = findAll();
        List<Category> subCategories = categories.stream().filter(p -> (p.getParent()!=null)).collect(Collectors.toList());
        return subCategories;
    }

    public Category findOne(int id){
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Category category){
        categoryRepository.save(category);
    }

    @Transactional
    public void delete(Category category){
        categoryRepository.delete(category);
    }

}
