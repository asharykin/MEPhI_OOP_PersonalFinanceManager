package ru.mephi.finance.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mephi.finance.model.Entity;
import ru.mephi.finance.model.User;
import ru.mephi.finance.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityService<? extends Entity> migratingService;

    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, List.of(migratingService));

        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("admin");
        testUser.setPassword("1234");
    }

    @AfterEach
    void tearDown() {
        if (UserService.isUserAuthorized()) {
            userService.deauthorize();
        }
    }

    @Test
    void isUserAuthorized_InitiallyFalse() {
        assertFalse(UserService.isUserAuthorized());
        assertNull(UserService.getCurrentUser());
    }

    @Test
    void createUser_Success() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        userService.createUser("new_user", "pass");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_DuplicateUsername_ThrowsException() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser("admin", "any_pass")
        );
    }

    @Test
    void authorize_Success_TriggersLoadData() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        userService.authorize("admin", "1234");

        assertTrue(UserService.isUserAuthorized());
        assertEquals(testUser, UserService.getCurrentUser());

        verify(migratingService, times(1)).loadData();
    }

    @Test
    void authorize_WrongCredentials_ThrowsException() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        assertThrows(NoSuchElementException.class,
                () -> userService.authorize("admin", "wrong_pass")
        );
        assertFalse(UserService.isUserAuthorized());
    }

    @Test
    void deauthorize_ClearsUserAndTriggersDumpData() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        userService.authorize("admin", "1234");

        userService.deauthorize();

        assertFalse(UserService.isUserAuthorized());
        assertNull(UserService.getCurrentUser());

        verify(migratingService, times(1)).dumpData();
    }
}
