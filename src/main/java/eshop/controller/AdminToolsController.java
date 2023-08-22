package eshop.controller;

import eshop.service.ProductService;
import eshop.service.UserService;
import eshop.service.VendorService;
import eshop.util.UniqueStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminToolsController {

    private final ProductService productService;

    private final UserService userService;

    private final VendorService vendorService;

    @PostMapping("/users/check_login")
    public @ResponseBody UniqueStatus checkLoginUnique(@RequestParam(value = "id", required = false) UUID id,
                                                       @RequestParam("login") String login,
                                                       @RequestParam("email") String email) {
        log.info("got /users/check_login post request with parameters id: {} login: {} email: {}", id, login, email);
        return userService.isLoginAndEmailUnique(id, login, email);
    }

    @PostMapping("/products/check_unique")
    public @ResponseBody Boolean checkProductUnique(@RequestParam(value = "id", required = false) UUID id,
                                                    @RequestParam("title") String title) {
        log.info("got /products/check_unique post request with parameters id: {} title: {}", id, title);
        return productService.isProductUnique(id, title);
    }

    @PostMapping("/vendors/check")
    public @ResponseBody Boolean checkVendorUnique(@RequestParam(value = "id", required = false) UUID id,
                                                        @RequestParam("title") String title) {
        log.info("got /vendors/check post request with parameters id: {} title: {}", id, title);
        return vendorService.isVendorUnique(id, title);
    }
}
