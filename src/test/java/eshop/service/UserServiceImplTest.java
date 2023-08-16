package eshop.service;

import eshop.model.User;
import eshop.repository.UserRepository;
import eshop.service.implementation.UserServiceImpl;
import eshop.util.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldFindOldPassportWhenNewOneIsEmpty() {
        List<User> users = getUsers();
        users.get(0).setPassword("oldEncodedPassword");
        User user = copyUser(users.get(0));
        user.setPassword("");

        Mockito.when(userRepository.findById(user.getId())).
            thenReturn(Optional.of(users.get(0)));

        try {
            userService.encodePassword(user);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        String result = user.getPassword();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("oldEncodedPassword", result);
    }

    @Test
    public void shouldEncodeNewPassword() {
        List<User> users = getUsers();
        users.get(0).setPassword("encoded");
        User user = copyUser(users.get(0));
        user.setPassword("newPassword");

        Mockito.when(passwordEncoder.encode(user.getPassword())).
            thenReturn("encoded");

        try {
            userService.encodePassword(user);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        String result = user.getPassword();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("encoded", result);
    }

    @Test
    public void shouldThrowWhenUserIsNotFound() {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setPassword("");

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        Mockito.when(userRepository.findById(user.getId())).
            thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.encodePassword(user));
    }


    private List<User> getUsers() {
        User firstUser = User.builder()
            .id(UUID.randomUUID())
            .username("test_user1")
            .email("test1@mail.ru")
            .build();
        User secondUser = User.builder()
            .id(UUID.randomUUID())
            .username("test_user2")
            .email("test2@mail.ru")
            .build();
        return List.of(firstUser, secondUser);
    }

    private User copyUser(User user) {
        return User.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .build();
    }

}

