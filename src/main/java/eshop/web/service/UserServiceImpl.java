package eshop.web.service;

import eshop.web.model.User;
import eshop.web.model.UserInformation;
import eshop.web.repository.UserInformationRepository;
import eshop.web.repository.UserRepository;
import eshop.web.util.Role;
import eshop.web.util.UniqueStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserInformationRepository userInformationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByUsername(name)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userInformationRepository.delete(user.getUserInformation());
        userRepository.delete(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        log.info("saving user: {}", user);
        if (user.getId() == null && user.getUserInformation() == null){
            enrichNewUser(user);
            log.info("new user enriched");
        }

        if (user.getPassword().isEmpty()) {
            log.info("password is empty, trying to find old password in database");
            String OldPassword = userRepository.findById(user.getId()).get().getPassword();
            user.setPassword(OldPassword);
            log.info("password successfully set: {}", OldPassword);
        } else {
            log.info("encoding new password for user");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("password successfully encoded");
        }

        userRepository.save(user);
        userInformationRepository.save(user.getUserInformation());
    }

    @Override
    public void getCurrentUserIntoModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = findByName(authentication.getName());
        model.addAttribute("user" ,user);
        model.addAttribute("userDetails" ,user.getUserInformation());
        log.info("added current user into model");
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = findByName(authentication.getName());
        return user;
    }

    @Override
    public UniqueStatus isLoginAndEmailUnique(UUID id, String login, String email) {
        log.info("checking if user is unique for name: {} and email: {} and ID: {}", login, email, id);
        boolean isUsernameNotUnique;
        boolean isEmailNotUnique;

        if (id == null) {
            log.info("user is new");
            isUsernameNotUnique = userRepository.existsByUsername(login);
            isEmailNotUnique = userRepository.existsByEmail(email);
        } else {
            log.info("user is not new, trying to find another user with the same login and email");
            isUsernameNotUnique = userRepository.existsByUsernameAndIdNot(login, id);
            isEmailNotUnique = userRepository.existsByEmailAndIdNot(email, id);
        }

        if (isUsernameNotUnique) {
            log.info("user is not unique, found another user with the same username: {}", login);
            return UniqueStatus.DUPLICATE_USERNAME;
        }
        if (isEmailNotUnique) {
            log.info("user is not unique, found another user with the same email: {}", email);
            return UniqueStatus.DUPLICATE_EMAIL;
        }

        log.info("user is unique");
        return UniqueStatus.OK;
    }

    @Override
    public void connectUserWithInfo(User user, UserInformation userInformation) {
        log.info("connecting user information with user {} {}", user, userInformation);
        userInformation.setUser(user);
        userInformation.setUserId(user.getId());
        user.setUserInformation(userInformation);
        log.info("connected: {}", user);
    }

    private void enrichNewUser(User user) {
        user.setRole(Role.USER);
        user.setUserInformation(new UserInformation());
        user.getUserInformation().setUser(user);
        user.setOrders(new ArrayList<>());
    }
}
