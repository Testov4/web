package eshop.web.services;

import eshop.web.models.User;
import eshop.web.models.UserInformation;
import eshop.web.repositories.UserInformationRepository;
import eshop.web.repositories.UserRepository;
import eshop.web.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserInformationRepository userInformationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       UserInformationRepository userInformationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInformationRepository = userInformationRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findOne(int id){
        return userRepository.findById(id).orElse(null);
    }

    public User findByName(String name){
        return userRepository.findByUsername(name);
    }

    public UserInformation findForUser(User user){
        return userInformationRepository.findById(user.getId()).orElse(null);
    }

    @Transactional
    public void delete(User user){
        userInformationRepository.delete(user.getUserInformation());
        userRepository.delete(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void save(User user){
        if (!checkEqualPasswordOrNull(user))
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole()==null) {
            user.setRole(Role.USER);
        }
        userRepository.save(user);
        userInformationRepository.save(user.getUserInformation());
    }

    @Transactional
    public void register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setUserInformation(new UserInformation());
        user.getUserInformation().setUser(user);
        userRepository.save(user);
        userInformationRepository.save(user.getUserInformation());
    }

    public boolean checkEqualPasswordOrNull(User user){
        User oldUser = userRepository.findByEmail(user.getEmail());

        if (oldUser==null)
            return false;

        if (user.getPassword().equals(oldUser.getPassword()))
            return true;

        if (user.getPassword().isEmpty()){
            user.setPassword(oldUser.getPassword());
            return true;
        }

        return false;
    }

    public void getCurrentUserIntoModel(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = findByName(currentUserName);
            model.addAttribute("user" ,user);
            model.addAttribute("userDetails" ,user.getUserInformation());
        }
    }

    public User getCurrent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = findByName(currentUserName);
            return user;
        }
        return null;
    }

    public String isLoginAndEmailUnique(Integer id, String login, String email) {
        User foundUser = userRepository.findByEmail(email);
        boolean userIsNew = (id == null);
        if (userIsNew) {
            if (foundUser != null)
                return "Duplicate Email";
        } else {
            if (!Objects.equals(foundUser.getId(), id)) {
                return "Duplicate Email";
            }else
            {
                foundUser = userRepository.findByUsername(login);

                if (!Objects.equals(foundUser.getId(), id)) {
                    return "Duplicate Username";
                }
            }
        }

        return "OK";
    }
}
