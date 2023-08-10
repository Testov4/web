package eshop.web.controllers;

import eshop.web.models.Vendor;
import eshop.web.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminTools {

    private final CategoryService categoryService;

    private final MyUserDetailsService myUserDetailsService;

    private final OrderService orderService;

    private final ProductService productService;

    private final UserService userService;

    private final VendorService vendorService;

    @Autowired
    public AdminTools(CategoryService categoryService, MyUserDetailsService myUserDetailsService,
                           OrderService orderService, ProductService productService, UserService userService,
                           VendorService vendorService) {
        this.categoryService = categoryService;
        this.myUserDetailsService = myUserDetailsService;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.vendorService = vendorService;
    }

    //check unique from admin user edit form
    @PostMapping("/users/check_login")
    public @ResponseBody String checkLoginUnique(@Param("id") Integer id, @Param("login") String login,
                                                         @Param("email") String email) {
        return userService.isLoginAndEmailUnique(id, login, email);
    }


    //check unique from admin product edit form
    @PostMapping("/products/check_unique")
    public @ResponseBody String checkProductUnique(@Param("id") Integer id, @Param("title") String title) {
        return productService.isProductUnique(id, title);
    }

    @PostMapping("/vendors/check")
    public @ResponseBody String checkVendorUnique(@Param("id") Integer id, @Param("title") String title){
        return vendorService.isVendorUnique(id, title);
    }

}
