package eshop.web.controller;

import eshop.web.model.Product;
import eshop.web.service.CategoryService;
import eshop.web.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final CategoryService categoryService;

    @GetMapping("{id}")
    public String showProduct(Model model, @PathVariable UUID id) {
        Product product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "product/product-page";
    }
}
