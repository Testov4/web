package eshop.web.controller;

import eshop.web.model.Category;
import eshop.web.model.Product;
import eshop.web.model.User;
import eshop.web.model.UserInformation;
import eshop.web.service.CategoryService;
import eshop.web.service.OrderService;
import eshop.web.service.ProductService;
import eshop.web.service.UserService;
import eshop.web.util.Role;
import eshop.web.util.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final UserService userService;

    private final ProductService productService;

    private final CategoryService categoryService;

    private final OrderService orderService;

    private final UserValidator userValidator;

    @GetMapping("/category")
    public String showCategories(Model model) {
        List<Category> listEnabledCategories = categoryService.findMainCategories();
        model.addAttribute("listCategories", listEnabledCategories);
        return "category";
    }

    @GetMapping("/category/{id}")
    public String showSubCategories(Model model, @PathVariable UUID id) {
        log.info("trying to find all subcategories of category id: {}", id);
        List<Category> listSubCategories = categoryService.findSubCategoriesByParentId(id);
        log.info("found subcategories: {}", listSubCategories);

        Category currentCategory = categoryService.findCategoryById(id);
        List<Product> products = productService.findProductsByCategory(currentCategory);
        log.info("found products: {}", products);

        model.addAttribute("listCategories", listSubCategories);
        model.addAttribute("listProducts", products);
        return "category";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("listCategories", categoryService.findMainCategories());
        model.addAttribute("listProducts", productService.findRandomProducts(4));
        return "index";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user,
                                   @ModelAttribute("userInformation") UserInformation userInformation) {
        return "/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") User user,
                                      @ModelAttribute UserInformation userInformation,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/registration";
        }
        userService.connectUserWithInfo(user, userInformation);
        user.setRole(Role.USER);
        userService.saveUser(user);
        log.info("user created: {}", user);
        return "/registration";
    }

    @GetMapping("/search")
    public String searchProductsByKeyword(HttpServletRequest request, Model model) {
        String keyword = request.getParameter("keyword");
        List<Product> products = productService.findByNamePart(keyword);
        log.info("found products by part of the name: {}", products);

        model.addAttribute("totalItems",products.size());
        model.addAttribute("resultList", products);
        return "product/search_result";
    }

    @GetMapping("/payment")
    public String paymentForAllUsersOrders(Model model) {
        User user = userService.getCurrentUser();
        orderService.changeOrdersStatus(user.getOrders());
        return "redirect:/user/orders";
    }
}
