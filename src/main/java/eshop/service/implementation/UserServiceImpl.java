package eshop.service.implementation;

import eshop.repository.UserInformationRepository;
import eshop.repository.UserRepository;
import eshop.service.UserService;
import eshop.model.User;
import eshop.model.UserInformation;
import eshop.util.UniqueStatus;
import eshop.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserInformationRepository userInformationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(UUID id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userInformationRepository.delete(user.getUserInformation());
        userRepository.delete(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void encodePassword(User user) throws UserNotFoundException {
        if (user.getPassword().isEmpty()) {
            log.info("password is empty, trying to find old password in database");
            user.setPassword(userRepository.findById(user.getId())
                .map(User::getPassword)
                .orElseThrow(() -> new UserNotFoundException("User not found")));
            log.info("password successfully set: {}", user.getPassword());
        } else {
            log.info("encoding new password for user");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        log.info("saving user: {}", user);
        user = assignUserInformationIfPresent(user);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createUser(User user, UserInformation userInformation) {
        userInformation.setUser(user);
        user.setUserInformation(userInformation);
        userRepository.save(user);
    }

    @Override
    public void getCurrentUserIntoModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).
            orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user" ,user);
        model.addAttribute("userDetails" ,user.getUserInformation());
        log.info("added current user into model");
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UniqueStatus isLoginAndEmailUnique(UUID id, String username, String email) {
        log.info("checking if user is unique for name: {} and email: {} and ID: {}", username, email, id);
        boolean isUsernameUnique = userRepository.findByUsername(username)
            .filter(user -> !user.getId().equals(id))
            .isEmpty();
        boolean isEmailUnique = userRepository.findByEmail(email)
            .filter(user -> !user.getId().equals(id))
            .isEmpty();

        if (!isUsernameUnique) {
            log.info("user is not unique, found another user with the same username: {}", username);
            return UniqueStatus.DUPLICATE_USERNAME;
        }
        if (!isEmailUnique) {
            log.info("user is not unique, found another user with the same email: {}", email);
            return UniqueStatus.DUPLICATE_EMAIL;
        }
        log.info("user is unique");
        return UniqueStatus.OK;
    }

    @Override
    public void copyFromNewUserInfoToCurrent(User user, UserInformation userInformation) {
        log.info("copying user information from user {} {}", user, userInformation);
        user.getUserInformation().setName(userInformation.getName());
        user.getUserInformation().setSurname(userInformation.getSurname());
        user.getUserInformation().setPhone(userInformation.getPhone());
        user.getUserInformation().setAddress(userInformation.getAddress());
        log.info("copied: {}", user);
    }

    private User assignUserInformationIfPresent(User user) {
        userInformationRepository.findByUser(user).ifPresent(i -> {
            UserInformation newInformation = user.getUserInformation();
            user.setUserInformation(i);
            copyFromNewUserInfoToCurrent(user, newInformation);
        });
        return user;
    }

}
