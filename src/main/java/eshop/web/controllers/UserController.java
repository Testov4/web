package eshop.web.controllers;

import eshop.web.models.User;
import eshop.web.models.UserInformation;
import eshop.web.services.OrderService;
import eshop.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/profile")
    public String userPage(Model model){
        userService.getCurrentUserIntoModel(model);
        return "user/user-main";
    }

    @GetMapping("/edit")
    public String editPage(Model model){
        userService.getCurrentUserIntoModel(model);
        return "user/user-edit";
    }

    @PostMapping("/edit")
    public String editUser(Model model, @ModelAttribute User user){
        //берем юзера из контекста чтобы сохранить в объект из формы айдишник+имейл
        User oldUser = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            oldUser = userService.findByName(currentUserName);
        }
        user.setId(oldUser.getId());
        user.setEmail(oldUser.getEmail());
        user.getUserInformation().setUserId(user.getId());


        userService.save(user);
        return "redirect:profile";
    }

    @GetMapping("/orders")
    public String showPaidOrders(Model model){
        //TODO
        User user = userService.getCurrent();
        model.addAttribute("orders", orderService.findPaidOrdersForUser(user));
        return "user/orders";
    }




}
