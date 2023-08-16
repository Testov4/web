package eshop.controller;

import eshop.service.OrderService;
import eshop.service.UserService;
import eshop.model.User;
import eshop.util.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String editUser(@ModelAttribute User user) throws UserNotFoundException {
        log.info("trying to get the information about current user");
        User currentUser = userService.getCurrentUser();
        log.info("got current user: {}", currentUser);
        currentUser.setUsername(user.getUsername());
        if (!user.getPassword().isEmpty())
        {
            currentUser.setPassword(user.getPassword());
            userService.encodePassword(currentUser);
        }
        log.info("added current user information to updated user: {}", user);

        userService.copyFromNewUserInfoToCurrent(currentUser, user.getUserInformation());
        userService.saveUser(currentUser);
        return "redirect:profile";
    }

    @GetMapping("/orders")
    public String showPaidOrders(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("orders", orderService.findPaidOrdersForUser(user));
        return "user/orders";
    }
}
