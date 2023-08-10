package eshop.web.controllers;

import eshop.web.models.Product;
import eshop.web.services.CategoryService;
import eshop.web.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("{id}")
    public String showProduct(Model model, @PathVariable int id){
        Product product = productService.findOne(id);
        model.addAttribute("product", product);
        return "product/product-page";
    }

    @PostMapping("{id}/{quant}")
    public String addToCart(Model model, @PathVariable int id, @PathVariable int quant){
        Product product = productService.findOne(id);
        System.out.println("DAUN VSE NORM " +product.getName() + " koli4:" + quant);
        return "product/product-page";
    }
}
