package ru.mephi.finance.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mephi.finance.model.User;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest { // для остальных репозиториев будет аналогично

    private UserRepository userRepository;
    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();

        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setPassword("password");
    }

    @Test
    void save_AndFindById_Success() {
        userRepository.save(testUser);

        Optional<User> found = userRepository.findById(1);

        assertTrue(found.isPresent());
        assertEquals("testUser", found.get().getUsername());
    }

    @Test
    void save_UpdatesExistingEntity() {
        userRepository.save(testUser);

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("newName");
        userRepository.save(updatedUser);

        Optional<User> found = userRepository.findById(1);
        assertEquals("newName", found.get().getUsername());
        assertEquals(1, userRepository.findAll().size());
    }

    @Test
    void delete_RemovesEntity() {
        userRepository.save(testUser);
        assertTrue(userRepository.findById(1).isPresent());

        userRepository.delete(testUser);

        assertFalse(userRepository.findById(1).isPresent());
        assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    void findAll_ReturnsAllEntities() {
        User user2 = new User();
        user2.setId(2);

        userRepository.save(testUser);
        userRepository.save(user2);

        Collection<User> users = userRepository.findAll();

        assertEquals(2, users.size());
        assertTrue(users.contains(testUser));
        assertTrue(users.contains(user2));
    }

    @Test
    void deleteAll_ClearsRepository() {
        userRepository.save(testUser);
        assertFalse(userRepository.findAll().isEmpty());

        userRepository.deleteAll();

        assertTrue(userRepository.findAll().isEmpty());
    }

    @Test
    void getLastId_ReturnsCorrectId() {
        assertEquals(0, userRepository.getLastId());

        userRepository.save(testUser);
        assertEquals(1, userRepository.getLastId());

        User user5 = new User();
        user5.setId(5);
        userRepository.save(user5);

        assertEquals(5, userRepository.getLastId());
    }

    @Test
    void findById_ReturnsEmptyForNonExistent() {
        Optional<User> found = userRepository.findById(999);
        assertTrue(found.isEmpty());
    }
}
