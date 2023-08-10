package eshop.web.controllers;

import eshop.web.models.*;
import eshop.web.services.*;
import eshop.web.util.Role;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;

    private final MyUserDetailsService myUserDetailsService;

    private final OrderService orderService;

    private final ProductService productService;

    private final UserService userService;

    private final VendorService vendorService;

    @Autowired
    public AdminController(CategoryService categoryService, MyUserDetailsService myUserDetailsService,
                           OrderService orderService, ProductService productService, UserService userService,
                           VendorService vendorService) {
        this.categoryService = categoryService;
        this.myUserDetailsService = myUserDetailsService;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.vendorService = vendorService;
    }

    @GetMapping
    public String showAdminPanel(){
        return "admin/admin-panel";
    }

    @GetMapping("categories")
    public String showCategoryPage(Model model){
        model.addAttribute("categories" ,categoryService.findAll());
        return "admin/category/categories";
    }

    @GetMapping("categories/delete/{id}")
    public String deleteCategory(@PathVariable int id){
        Category category = categoryService.findOne(id);
        categoryService.delete(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("categories/edit/{id}")
    public String showCategoryEditPage(Model model, @PathVariable int id){
        model.addAttribute("category", categoryService.findOne(id));
        return "admin/category/category_form";
    }

    @GetMapping("categories/new")
    public String showNewCategoryPage(@ModelAttribute("category") Category category){
        return "admin/category/category_new";
    }

    @PostMapping("categories/new")
    public String submitNew(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "admin/category/category_new";
        if (category.getParent().getId()==0)
            category.setParent(null);
        categoryService.save(category);

        return "redirect:/admin/categories";
    }

    @PostMapping("categories/edit/{id}")
    public String submitEdit(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "admin/category/category_form";

        categoryService.save(category);

        return "redirect:/admin/categories";
    }

    @GetMapping("users")
    public String listUsers(Model model){
        model.addAttribute("users", userService.findAll());
        return "admin/user/users";
    }

    @GetMapping("users/delete/{id}")
    public String deleteUser(@PathVariable int id){
        User user = userService.findOne(id);
        userService.delete(user);
        return "redirect:/admin/users";
    }


    @GetMapping("users/edit/{id}")
    public String showUserEditPage(Model model, @PathVariable int id){
        model.addAttribute("user", userService.findOne(id));
        model.addAttribute("userInformation", userService.findOne(id).getUserInformation());
        model.addAttribute("roles", Role.values());
        return "admin/user/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user, @ModelAttribute UserInformation userInformation){
        userInformation.setUser(user);
        userInformation.setUserId(user.getId());
        user.setUserInformation(userInformation);
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("userInformation", new UserInformation());
        model.addAttribute("roles", Role.values());
        return "admin/user/user_form";
    }

    @GetMapping("products")
    public String productsPage(Model model){
        model.addAttribute("products", productService.findAll());
        model.addAttribute("listCategories", categoryService.findAll());
        //listCategories
        return "admin/product/products";
    }

    @GetMapping("products/delete/{id}")
    public String deleteProduct(@PathVariable int id){
        Product product = productService.findOne(id);
        productService.delete(product);
        return "redirect:/admin/products";
    }

    @GetMapping("products/edit/{id}")
    public String showProductEditPage(Model model, @PathVariable int id){
        model.addAttribute("product", productService.findOne(id));
        model.addAttribute("vendorList", vendorService.findAll());
        model.addAttribute("categoryList", categoryService.findAllSubCategories());
        return "admin/product/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product){
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("products/new")
    public String showProductEditPage(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("vendorList", vendorService.findAll());
        model.addAttribute("categoryList", categoryService.findAllSubCategories());
        return "admin/product/product_form";
    }

    @GetMapping("products/search")
    public String searchProduct(HttpServletRequest request, Model model){
        String keyword = request.getParameter("keyword");
        String categoryId = request.getParameter("categoryId");
        int catId = Integer.parseInt(categoryId);
        List<Product> products = productService.findForSearchWithCategory(keyword, categoryService.findOne(catId));
        model.addAttribute("products", products);
        model.addAttribute("listCategories", categoryService.findAll());
        //listCategories
        return "admin/product/products";
    }

    @GetMapping("vendors")
    public String vendorsPage(Model model){
        model.addAttribute("vendors", vendorService.findAll());
        return "admin/vendor/vendors";
    }

    @GetMapping("vendors/edit/{id}")
    public String vendorEdit(Model model, @PathVariable int id){
        model.addAttribute("vendor", vendorService.findOne(id));
        return "admin/vendor/vendor_form";
    }

    @PostMapping("vendors/save")
    public String vendorSave(@ModelAttribute Vendor vendor){
        vendorService.save(vendor);
        return "redirect:/admin/vendors";
    }

    @GetMapping("vendors/new")
    public String vendorCreate(Model model){
        model.addAttribute("vendor", new Vendor());
        return "admin/vendor/vendor_form";
    }

    @GetMapping("vendors/delete/{id}")
    public String deleteVendor(Model model, @PathVariable int id){
        vendorService.delete(vendorService.findOne(id));
        return "redirect:/admin/vendors";
    }

    @GetMapping("orders")
    public String ordersPage(Model model){
        model.addAttribute("orders", orderService.findAll());
        return "admin/orders/orders";
    }

    @GetMapping("orders/delete/{id}")
    public String deleteOrder(Model model, @PathVariable int id){
        orderService.delete(orderService.findOne(id));
        return "redirect:/admin/orders";
    }

    @GetMapping("orders/edit/{id}")
    public String orderEdit(Model model, @PathVariable int id){
        model.addAttribute("order", orderService.findOne(id));
        model.addAttribute("OrderStatus", OrderStatus.values());
        return "admin/orders/order_form";
    }

    @PostMapping("orders/save")
    public String orderSave(@ModelAttribute Order order){
        order.setQuantity(orderService.findOne(order.getId()).getQuantity());
        order.setProduct(orderService.findOne(order.getId()).getProduct());
        orderService.save(order);
        return "redirect:/admin/orders";
    }
}
