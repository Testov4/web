package eshop.controller;

import eshop.service.OrderService;
import eshop.service.ProductService;
import eshop.service.UserService;
import eshop.model.Order;
import eshop.model.Product;
import eshop.util.OrderNotFoundException;
import eshop.util.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/basket")
@Slf4j
public class CartController {

    private final UserService userService;

    private final ProductService productService;

    private final OrderService orderService;

    @GetMapping
    public String showCart(Model model) {
    userService.getCurrentUserIntoModel(model);
    model.addAttribute("orderBaskets", orderService.findOrdersForBasket(userService.getCurrentUser()));
    return "/shopping-cart";
    }

    @PostMapping("add/{id}/{quantity}")
    public String addToCart(@PathVariable UUID id, @PathVariable Integer quantity) throws ProductNotFoundException {
        Product product = productService.findProductById(id);
        Order newOrder = orderService.buildNewOrder(userService.getCurrentUser() , product,
            userService.getCurrentUser().getUserInformation().getAddress(), quantity);
        orderService.saveOrder(newOrder);
        log.info("order saved: {}", newOrder);
        return "redirect:profile";
    }

    @PostMapping("remove/{id}")
    public String removeFromCart(@PathVariable UUID id) throws OrderNotFoundException {
        Order order = orderService.findOrderById(id);
        orderService.deleteOrder(order);
        log.info("order deleted");
        return "redirect:/basket";
    }
}
