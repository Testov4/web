package eshop.service;

import eshop.model.User;
import eshop.model.UserInformation;
import eshop.util.UniqueStatus;
import eshop.util.UserNotFoundException;
import org.springframework.ui.Model;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> findAllUsers();

    User findUserById(UUID id) throws UserNotFoundException;

    void deleteUser(User user);

    void encodePassword(User user) throws UserNotFoundException;

    User findUserByEmail(String email);

    void saveUser(User user);

    void createUser(User user, UserInformation userInformation);

    void getCurrentUserIntoModel(Model model);

    User getCurrentUser();

    UniqueStatus isLoginAndEmailUnique(UUID id, String login, String email);

    void copyFromNewUserInfoToCurrent(User user, UserInformation userInformation);


}
