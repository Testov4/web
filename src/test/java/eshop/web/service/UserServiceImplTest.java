package eshop.web.service;

import eshop.web.model.User;
import eshop.web.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.security.PublicKey;
import java.util.List;
import java.util.UUID;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    private List<User> getUsers() {
        User firstUser = User.builder()
            .id(UUID.randomUUID())
            .username("testUser1")
            .email("right@mail.ru")
            .build();
        User secondUser = User.builder()
            .id(UUID.randomUUID())
            .username("testUser1")
            .email("wrong@mail.ru")
            .build();
        return List.of(firstUser, secondUser);
    }

}

