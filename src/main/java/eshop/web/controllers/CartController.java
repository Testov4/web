package eshop.web.controllers;

import eshop.web.models.Order;
import eshop.web.models.Product;
import eshop.web.models.User;
import eshop.web.services.OrderService;
import eshop.web.services.ProductService;
import eshop.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/basket")
public class CartController {

    private final UserService userService;
    private final ProductService productService;

    private final OrderService orderService;

    @Autowired
    public CartController(ProductService productService, UserService userService, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public String showCart(Model model){
    userService.getCurrentUserIntoModel(model);
    model.addAttribute("orderBaskets", orderService.findOrdersForBasket(userService.getCurrent()));
    return "/shopping-cart";
    }

    @PostMapping("add/{id}/{quant}")
    public String addToCart(Model model, @PathVariable int id, @PathVariable int quant){
        Product product = productService.findOne(id);
        Order newOrder = orderService.newOrder(userService.getCurrent() , product,
                userService.getCurrent().getUserInformation().getAddress(), quant);
        orderService.save(newOrder);
        return "redirect:profile";
    }

    @PostMapping("remove/{id}")
    public String removeFromCart(Model model, @PathVariable int id){
        Order order = orderService.findOne(id);
        orderService.delete(order);
        return "redirect:/basket";
    }
}
