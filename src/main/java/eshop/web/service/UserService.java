package eshop.web.service;

import eshop.web.model.User;
import eshop.web.model.UserInformation;
import eshop.web.util.UniqueStatus;
import org.springframework.ui.Model;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> findAllUsers();

    User findUserById(UUID id);

    User findByName(String name);

    void deleteUser(User user);

    User findUserByEmail(String email);

    void saveUser(User user);

    void getCurrentUserIntoModel(Model model);

    User getCurrentUser();

    UniqueStatus isLoginAndEmailUnique(UUID id, String login, String email);

    void connectUserWithInfo(User user, UserInformation userInformation);
}
