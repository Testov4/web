package eshop.web.util;

import eshop.web.models.User;
import eshop.web.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        //check email
        if (userService.findByEmail(user.getEmail())!=null){
            errors.rejectValue("email", "", "This email is already taken");
        }


    }
}
