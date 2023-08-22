package eshop.controller;

import eshop.model.Product;
import eshop.service.ProductService;
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

    @GetMapping("{id}")
    public String showProduct(Model model, @PathVariable UUID id) {
        Product product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "product/product-page";
    }
}
