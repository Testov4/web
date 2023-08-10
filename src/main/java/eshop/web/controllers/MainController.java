package eshop.web.controllers;

import eshop.web.models.Category;
import eshop.web.models.OrderStatus;
import eshop.web.models.Product;
import eshop.web.models.User;
import eshop.web.services.CategoryService;
import eshop.web.services.OrderService;
import eshop.web.services.ProductService;
import eshop.web.services.UserService;
import eshop.web.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private final UserService userService;

    private final ProductService productService;

    private final CategoryService categoryService;

    private final OrderService orderService;

    private final UserValidator userValidator;

    @Autowired
    public MainController(UserService userService, ProductService productService,
                          CategoryService categoryService, UserValidator userValidator, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.userValidator = userValidator;
        this.orderService = orderService;
    }

    @GetMapping("/category")
    public String showCategories(Model model) {
        List<Category> listEnabledCategories = categoryService.findMainCategories();
        model.addAttribute("listCategories", listEnabledCategories);
        return "category";
    }

    @GetMapping("/category/{id}")
    public String showSubCategories(Model model, @PathVariable int id) {
        List<Category> listEnabledCategories = categoryService.findSubCategories(id);
        Category currentCategory = categoryService.findOne(id);
        List<Product> products = productService.findByCategory(currentCategory);
        model.addAttribute("listCategories", listEnabledCategories);
        model.addAttribute("listProducts", products);
        return "category";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Category> listEnabledCategories = categoryService.findMainCategories();
        model.addAttribute("listCategories", listEnabledCategories);
        List<Product> randomProducts = productService.findRandomItems(4);
        model.addAttribute("listProducts", randomProducts);
        return "index";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user){
        return "/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "/registration";
        userService.register(user);
        return "redirect:";
    }

    @GetMapping("/search")
    public String search(HttpServletRequest request, Model model){
        String keyword = request.getParameter("keyword");
        List<Product> products = productService.findForSearch(keyword);
        model.addAttribute("totalItems",products.size());
        model.addAttribute("resultList", products);
        return "product/search_result";
    }

    @GetMapping("/payment")
    public String search(Model model){
        User user = userService.getCurrent();
        user.getOrders().stream()
                .forEach(p -> {
                    p.setOrderStatus(OrderStatus.ORDERING);
                    orderService.save(p);
                });
        return "redirect:/user/orders";
    }

}
