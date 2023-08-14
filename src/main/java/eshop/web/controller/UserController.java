package eshop.web.controller;

import eshop.web.model.User;
import eshop.web.service.OrderService;
import eshop.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private final OrderService orderService;

    @GetMapping("/profile")
    public String userPage(Model model) {
        userService.getCurrentUserIntoModel(model);
        return "user/user-main";
    }

    @GetMapping("/edit")
    public String editPage(Model model) {
        userService.getCurrentUserIntoModel(model);
        return "user/user-edit";
    }

    @PostMapping("/edit")
    public String editUser(Model model, @ModelAttribute User user) {
        log.info("trying to get the information about current user");
        User oldUser = userService.getCurrentUser();
        log.info("got current user: {}", oldUser);
        user.setId(oldUser.getId());
        user.setEmail(oldUser.getEmail());
        user.setRole(oldUser.getRole());
        log.info("added current user information to updated user: {}", user);

        userService.connectUserWithInfo(user, user.getUserInformation());
        userService.saveUser(user);
        return "redirect:profile";
    }

    @GetMapping("/orders")
    public String showPaidOrders(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("orders", orderService.findPaidOrdersForUser(user));
        return "user/orders";
    }
}
