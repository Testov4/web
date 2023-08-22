package eshop.service;

import eshop.model.Category;
import eshop.model.Product;
import eshop.repository.ProductRepository;
import eshop.service.implementation.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    static List<Product> products;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    private void getProducts() {
        Category category1 = Category.builder()
            .name("test_category1")
            .build();
        Category category2 = Category.builder()
            .name("test_category2")
            .build();

        products = List.of(
            Product.builder()
                .id(UUID.randomUUID())
                .name("test_name1")
                .category(category1)
                .build(),
            Product.builder()
                .id(UUID.randomUUID())
                .name("test_name2")
                .category(category1)
                .build(),
            Product.builder()
                .id(UUID.randomUUID())
                .name("test_name3")
                .category(category2)
                .build()
        );
    }

    @Test
    public void shouldReturnTrueWhenNameIsNotFound() {
        UUID id = UUID.randomUUID();
        String name = "wrong_test_name";

        Mockito.when(productRepository.findByName(name)).
            thenReturn(products.stream().filter(product -> product.getName().equals(name)).findAny());

        Boolean result = productService.isProductUnique(id, name);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnTrueWhenNameIsAlreadyInListButSameId() {
        UUID id = products.get(0).getId();
        String name = products.get(0).getName();

        Mockito.when(productRepository.findByName(name)).
            thenReturn(products
                .stream()
                .filter(product -> product.getName().equals(name))
                .findAny());

        Boolean result = productService.isProductUnique(id, name);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenNameIsAlreadyInListAndRandomId() {
        UUID id = UUID.randomUUID();
        String name = products.get(0).getName();

        Mockito.when(productRepository.findByName(name)).
            thenReturn(products
                .stream()
                .filter(product -> product.getName().equals(name))
                .findAny());

        Boolean result = productService.isProductUnique(id, name);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldReturnEmptyListIfNameNotContains() {
        String name = "wrong";
        Category category = products.get(0).getCategory();

        Mockito.when(productRepository.findByNameContaining(name)).
            thenReturn(products
                .stream()
                .filter(product -> product.getName().contains(name))
                .collect(Collectors.toList()));

        List<Product> result = productService.findByCategoryAndNameContaining(name, category);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void shouldReturnEmptyListIfNotCorrectCategoryPassed() {
        String name = "test";
        Category category = Category.builder().name("wrong_category").build();

        Mockito.when(productRepository.findByNameContaining(name)).
            thenReturn(products
                .stream()
                .filter(product -> product.getName().contains(name))
                .collect(Collectors.toList()));

        List<Product> result = productService.findByCategoryAndNameContaining(name, category);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void shouldReturnEmptyListIfCategoryIsNull() {
        String name = "test";
        Category category = null;

        Mockito.when(productRepository.findByNameContaining(name)).
            thenReturn(products
                .stream()
                .filter(product -> product.getName().contains(name))
                .collect(Collectors.toList()));

        List<Product> result = productService.findByCategoryAndNameContaining(name, category);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void shouldReturnCorrectProducts() {
        String name = "test";
        Category category = products.get(0).getCategory();

        Mockito.when(productRepository.findByNameContaining(name)).
            thenReturn(products
                .stream()
                .filter(product -> product.getName().contains(name))
                .collect(Collectors.toList()));

        List<Product> result = productService.findByCategoryAndNameContaining(name, category);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
    }

}
