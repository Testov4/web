package eshop.service;

import eshop.model.User;
import eshop.repository.UserRepository;
import eshop.service.implementation.UserServiceImpl;
import eshop.exception.UserNotFoundException;
import eshop.util.UniqueStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    static List<User> users;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void prepareUsers() {
        users = List.of(
            User.builder()
                .id(UUID.randomUUID())
                .username("test_user1")
                .email("test1@mail.ru")
                .build(),
            User.builder()
                .id(UUID.randomUUID())
                .username("test_user2")
                .email("test2@mail.ru")
                .build()
        );
    }

    @Test
    public void shouldFindOldPassportWhenNewOneIsEmpty() {
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

    @Test
    public void shouldReturnOkWhenEverythingStaysSame() {
        User user = copyUser(users.get(0));
        UUID id = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();

        Mockito.when(userRepository.findByUsername(username)).
            thenReturn(users
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny());
        Mockito.when(userRepository.findByEmail(email)).
            thenReturn(users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny());

        UniqueStatus result = userService.isLoginAndEmailUnique(id, username, email);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(UniqueStatus.OK, result);
    }

    @Test
    public void shouldReturnOkUsernameAndEmailIsUnique() {
        User user = copyUser(users.get(0));
        UUID id = user.getId();
        String username = "new_name";
        String email = "newmail@mail.ru";

        Mockito.when(userRepository.findByUsername(username)).
            thenReturn(users
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny());
        Mockito.when(userRepository.findByEmail(email)).
            thenReturn(users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny());

        UniqueStatus result = userService.isLoginAndEmailUnique(id, username, email);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(UniqueStatus.OK, result);
    }

    @Test
    public void shouldReturnDuplicateUsername() {
        User user = copyUser(users.get(0));
        UUID id = null;
        String username = user.getUsername();
        String email = "newmail@mail.ru";

        Mockito.when(userRepository.findByUsername(username)).
            thenReturn(users
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny());
        Mockito.when(userRepository.findByEmail(email)).
            thenReturn(users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny());

        UniqueStatus result = userService.isLoginAndEmailUnique(id, username, email);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(UniqueStatus.DUPLICATE_USERNAME, result);
    }

    @Test
    public void shouldReturnDuplicateEmail() {
        User user = copyUser(users.get(0));
        UUID id = UUID.randomUUID();
        String username =  "new_name";
        String email = user.getEmail();

        Mockito.when(userRepository.findByUsername(username)).
            thenReturn(users
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny());
        Mockito.when(userRepository.findByEmail(email)).
            thenReturn(users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny());

        UniqueStatus result = userService.isLoginAndEmailUnique(id, username, email);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(UniqueStatus.DUPLICATE_EMAIL, result);
    }

    @Test
    public void shouldReturnDuplicateEmailWithOldUsernameAndNewNotUniqueEmail() {
        User user = copyUser(users.get(0));
        UUID id = user.getId();
        String username =  user.getUsername();
        String email = users.get(1).getEmail();

        Mockito.when(userRepository.findByUsername(username)).
            thenReturn(users
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny());
        Mockito.when(userRepository.findByEmail(email)).
            thenReturn(users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny());

        UniqueStatus result = userService.isLoginAndEmailUnique(id, username, email);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(UniqueStatus.DUPLICATE_EMAIL, result);
    }

    @Test
    public void shouldReturnDuplicateUsernameWithOldEmailAndNewNotUniqueUsername() {
        User user = copyUser(users.get(0));
        UUID id = user.getId();
        String username =  users.get(1).getUsername();
        String email = user.getEmail();

        Mockito.when(userRepository.findByUsername(username)).
            thenReturn(users
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findAny());
        Mockito.when(userRepository.findByEmail(email)).
            thenReturn(users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny());

        UniqueStatus result = userService.isLoginAndEmailUnique(id, username, email);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(UniqueStatus.DUPLICATE_USERNAME, result);
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

