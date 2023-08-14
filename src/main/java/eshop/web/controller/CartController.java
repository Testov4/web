package eshop.web.controller;

import eshop.web.model.Order;
import eshop.web.model.Product;
import eshop.web.service.OrderService;
import eshop.web.service.ProductService;
import eshop.web.service.UserService;
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
@RequestMapping("/basket")
@RequiredArgsConstructor
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

    @PostMapping("add/{id}/{quant}")
    public String addToCart(Model model, @PathVariable UUID id, @PathVariable int quant) {
        Product product = productService.findProductById(id);
        Order newOrder = orderService.createNewOrder(userService.getCurrentUser() , product,
                userService.getCurrentUser().getUserInformation().getAddress(), quant);
        orderService.saveOrder(newOrder);
        log.info("order saved: {}", newOrder);
        return "redirect:profile";
    }

    @PostMapping("remove/{id}")
    public String removeFromCart(Model model, @PathVariable UUID id) {
        Order order = orderService.FindOrderById(id);
        orderService.deleteOrder(order);
        log.info("order deleted");
        return "redirect:/basket";
    }
}
