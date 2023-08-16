package eshop.controller;

import eshop.service.CategoryService;
import eshop.service.OrderService;
import eshop.service.UserService;
import eshop.model.Category;
import eshop.model.Product;
import eshop.model.User;
import eshop.model.UserInformation;
import eshop.service.ProductService;
import eshop.util.Role;
import eshop.util.UserNotFoundException;
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
    public String performRegistration(@ModelAttribute User user,
                                      @ModelAttribute UserInformation userInformation,
                                      BindingResult bindingResult) throws UserNotFoundException {
        if (bindingResult.hasErrors()) {
            return "/registration";
        }
        log.info("user {}", user);

        log.info("userInf {}", userInformation);
        user.setRole(Role.USER);
        user.setUserInformation(userInformation);
        userInformation.setUser(user);
        userService.encodePassword(user);
        userService.createUser(user, userInformation);
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
    public String paymentForAllUsersOrders() {
        User user = userService.getCurrentUser();
        orderService.changeOrdersStatus(user.getOrders());
        return "redirect:/user/orders";
    }
}
